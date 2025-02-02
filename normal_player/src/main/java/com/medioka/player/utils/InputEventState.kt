package com.medioka.player.utils

import androidx.annotation.IntRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce

class InputEventState internal constructor(@IntRange(from = 0) private val seconds: Int) {
    private var onIdleTimeout: (() -> Unit)? = null
    private val channel = Channel<Int>(CONFLATED)

    fun setIdleTimeoutCallback(
        onIdleTimeout: () -> Unit,
        callbackBeforeIdleTimeOut: (() -> Unit)? = null,
        seconds: Int = this.seconds,
    ) {
        callbackBeforeIdleTimeOut?.invoke()
        this.onIdleTimeout = onIdleTimeout
        channel.trySend(seconds)
    }

    @OptIn(FlowPreview::class)
    suspend fun observe() {
        channel
            .consumeAsFlow()
            .cancellable()
            .debounce { it.toLong() * 1000 }
            .collect {
                onIdleTimeout?.invoke()
            }
    }
}

@Composable
fun rememberInputEventState(@IntRange(from = 0) seconds: Int) =
    remember { InputEventState(seconds = seconds) }
        .also {
            LaunchedEffect(it) {
                it.observe()
            }
        }
