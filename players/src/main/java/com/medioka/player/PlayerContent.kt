package com.medioka.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import com.example.core_ui.TrainingMobileTheme
import com.medioka.player.callback.ConfigCallback
import com.medioka.player.callback.ControlsCallback
import com.medioka.player.callback.PlaybackStateCallback
import com.medioka.player.component.ConfigButtons
import com.medioka.player.component.ControlButtons
import com.medioka.player.component.ControllerWithTimeStamp

private const val ASPECT_RATIO = 16f / 9f

@Composable
fun PlayerContent(
    modifier: Modifier = Modifier,
    shouldShowControllerProvider: () -> Boolean,
    isPlayingProvider: () -> Boolean,
    playbackStateProvider: () -> Int,
    configsCallback: ConfigCallback,
    controlsCallback: ControlsCallback,
    playbackStateCallback: PlaybackStateCallback,
    player: @Composable (Modifier) -> Unit
) {
    Box(modifier = modifier) {
        player(
            Modifier
                .fillMaxWidth()
                .aspectRatio(ASPECT_RATIO)
        )

        VisibilityWrapper(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ASPECT_RATIO),
            isVisibleProvider = {
                val isBuffering =
                    playbackStateProvider() == Player.STATE_IDLE || playbackStateProvider() == Player.STATE_BUFFERING
                shouldShowControllerProvider() || isBuffering
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(TrainingMobileTheme.colorScheme.primaryBackground.copy(0.5f))
            )
        }

        VisibilityWrapper(
            modifier = Modifier.align(Alignment.Center),
            isVisibleProvider = { !shouldShowControllerProvider() && (playbackStateProvider() == Player.STATE_IDLE || playbackStateProvider() == Player.STATE_BUFFERING) }
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = TrainingMobileTheme.colorScheme.textPrimary,
                strokeWidth = 2.dp
            )
        }

        VisibilityWrapper(
            modifier = Modifier.align(Alignment.TopEnd),
            isVisibleProvider = shouldShowControllerProvider
        ) {
            ConfigButtons(
                modifier = Modifier
                    .padding(top = 12.dp, end = 16.dp),
                isAutoPlayNextProvider = { true },
                onAutoPlayNextChange = {
                    // TODO: Here
                },
                onSettingClick = configsCallback::onSettingClick,
                onCaptionClick = configsCallback::onCaptionClick,
                onCastClick = configsCallback::onCastClick
            )
        }

        VisibilityWrapper(
            modifier = Modifier.align(Alignment.Center),
            isVisibleProvider = shouldShowControllerProvider
        ) {
            ControlButtons(
                modifier = Modifier.align(Alignment.Center),
                isPlayingProvider = isPlayingProvider,
                onPlay = controlsCallback::play,
                onPrevious = controlsCallback::previous,
                onNext = controlsCallback::next,
                onPause = controlsCallback::pause
            )

        }

        VisibilityWrapper(
            modifier = Modifier.align(Alignment.BottomStart),
            isVisibleProvider = shouldShowControllerProvider
        ) {
            ControllerWithTimeStamp(
                timeStampModifier = Modifier
                    .padding(bottom = 18.dp)
                    .padding(horizontal = 19.dp),
                playbackStateCallback = playbackStateCallback,
                onExpandVideo = {
                    // TODO: Expand Video
                }
            )
        }
    }
}

@Composable
fun VisibilityWrapper(
    modifier: Modifier,
    isVisibleProvider: () -> Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = isVisibleProvider(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        content()
    }
}

@Preview
@Composable
private fun PlayerContentPreview() {
    TrainingMobileTheme {
        PlayerContent(
            shouldShowControllerProvider = { true },
            isPlayingProvider = { true },
            configsCallback = object : ConfigCallback {
                override fun onCastClick() = Unit
                override fun onCaptionClick() = Unit
                override fun onSettingClick() = Unit
            },
            playbackStateProvider = { Player.STATE_IDLE },
            controlsCallback = object : ControlsCallback {
                override fun play() = Unit
                override fun pause() = Unit
                override fun next() = Unit
                override fun previous() = Unit
                override fun seekForward() = Unit
                override fun seekBackward() = Unit
            },
            playbackStateCallback = object : PlaybackStateCallback {
                override fun getCurrentPosition(): Long = 0L
                override fun getDuration(): Long = 0L
                override fun getBufferPercentage(): Int = 0
            },
            player = { itemModifier ->
                Box(itemModifier.background(Color.Black))
            }
        )
    }
}
