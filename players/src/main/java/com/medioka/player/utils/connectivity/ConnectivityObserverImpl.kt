package com.medioka.player.utils.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.PlaybackException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update


class ConnectivityObserverImpl(
    context: Context,
) : ConnectivityObserver {
    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
    private val _currentNetwork = MutableStateFlow(false)
    private val networkRequest by lazy { createNetworkRequester() }
    private val networkCallback by lazy { createNetworkCallback() }

    override val isNetworkConnectedFlow: StateFlow<Boolean> = _currentNetwork.asStateFlow()

    override val isNetworkConnected: Boolean = _currentNetwork.value

    override fun register() {
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        checkInitialNetworkConnection()
    }

    override fun unregister() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun createNetworkRequester(): NetworkRequest {
        return NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .build()
    }

    private fun createNetworkCallback(): NetworkCallback {
        val networkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                _currentNetwork.update { true }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                _currentNetwork.update { false }
            }

            override fun onUnavailable() {
                super.onUnavailable()
                _currentNetwork.update { false }
            }
        }

        return networkCallback
    }

    private fun checkInitialNetworkConnection() {
        // TODO: Need to check on ethernet too, some Android TV support ETHERNET
        val isConnected = connectivityManager.activeNetwork?.let { network ->
            connectivityManager.getNetworkCapabilities(network)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } ?: false

        _currentNetwork.update { isConnected }
    }
}

/**
 *  A connectivity observer wrapper, so it won't get recomposed everytime there is a recomposition.
 *  The callback is lifecycle aware, it will get registered `ON_START` and unregistered `ON_STOP`.
 **/
@Composable
fun rememberConnectivityObserver(context: Context): ConnectivityObserver {
    return remember {
        ConnectivityObserverImpl(context)
    }.also {
        val lifecycleOwner = LocalLifecycleOwner.current
        DisposableEffect(lifecycleOwner) {
            val lifecycleEvent = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> it.register()
                    Lifecycle.Event.ON_STOP -> it.unregister()
                    else -> Unit
                }
            }
            lifecycleOwner.lifecycle.addObserver(lifecycleEvent)

            onDispose {
                it.unregister()
            }
        }
    }
}

@Composable
fun PlayerConnectivityObserver(
    playbackExceptionProvider: () -> PlaybackException?,
    blockToRunOnNetworkReconnect: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val playbackException by rememberUpdatedState(playbackExceptionProvider())
    val connectivityObserver = rememberConnectivityObserver(context)

    LaunchedEffect(true) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            connectivityObserver.isNetworkConnectedFlow.collectLatest { isConnected ->
                val isErrorCauseConnection = when (playbackException?.errorCode) {
                    PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED, PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> true
                    else -> false
                }

                if (isConnected && isErrorCauseConnection) {
                    blockToRunOnNetworkReconnect()
                }
            }
        }
    }
}
