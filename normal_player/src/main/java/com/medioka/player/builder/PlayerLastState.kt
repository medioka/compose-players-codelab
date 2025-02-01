package com.medioka.player.builder

import androidx.compose.runtime.Stable
import androidx.media3.common.TrackSelectionParameters

@Stable
data class PlayerLastState(
    val playWhenReady: Boolean = true,
    val startDuration: Long = 0L,
    val playbackSpeed: Float = 1.0f,
    val trackSelectionParams: TrackSelectionParameters?
)