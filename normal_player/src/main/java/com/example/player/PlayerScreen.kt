package com.example.player

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.player.builder.rememberPlayerManager
import com.example.player.utils.connectivity.PlayerConnectivityObserver
import com.example.player.utils.findActivity

private const val URL = "https://storage.googleapis.com/wvmedia/clear/hevc/tears/tears.mpd"

@SuppressLint("SourceLockedOrientationActivity")
@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(modifier: Modifier = Modifier) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val playerManager = rememberPlayerManager(URL)

    // States
    val player by playerManager.player.collectAsStateWithLifecycle()
    val isPlaying by playerManager.isPlaying.collectAsStateWithLifecycle()
    val playbackException by playerManager.playbackException.collectAsStateWithLifecycle()
    val playbackState by playerManager.playbackState.collectAsStateWithLifecycle()
    var shouldShowController by remember { mutableStateOf(false) }

    DisposableEffect(lifecycleOwner) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}

        // TODO: Handle landscape orientation for player later
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val lifecycleEvent = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> playerManager.initialize(context = context)
                Lifecycle.Event.ON_STOP -> playerManager.release()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleEvent)

        onDispose {
            playerManager.release()
            activity.requestedOrientation = originalOrientation
        }
    }

    PlayerConnectivityObserver(
        playbackExceptionProvider = { playbackException },
        blockToRunOnNetworkReconnect = playerManager::playOnReconnect
    )

    PlayerContent(
        modifier = modifier,
        shouldShowControllerProvider = { shouldShowController },
        isPlayingProvider = { isPlaying },
        playbackStateProvider = { playbackState },
        configsCallback = playerManager.configCallback,
        controlsCallback = playerManager.controlsCallback,
        playbackStateCallback = playerManager.playbackStateCallback
    ) { itemModifier ->
        AndroidView(
            modifier = itemModifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    shouldShowController = !shouldShowController
                }
            ),
            factory = { context ->
                PlayerView(context).apply {
                    this.player = player
                    this.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                    this.useController = false
                }
            },
            update = { playerView ->
                playerView.player = player
            }
        )
    }
}