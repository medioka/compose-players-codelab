package com.example.player.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.player.R
import com.example.player.callback.PlaybackStateCallback
import com.example.player.custom.button.CustomIconButton
import com.example.player.ui.theme.TrainingMobileTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import millisToPlayerDurationFormat

private const val DELAY_INTERVAL = 500L

@Composable
fun ControllerWithTimeStamp(
    modifier: Modifier = Modifier,
    timeStampModifier: Modifier = Modifier,
    playbackStateCallback: PlaybackStateCallback,
    onExpandVideo: () -> Unit
) {
    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }
    var bufferPercentage by remember { mutableFloatStateOf(0f) }
    var currentPositionPercentage by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(true) {
        while (isActive) {
            playbackStateCallback.apply {
                currentPosition = getCurrentPosition()
                bufferPercentage = getBufferPercentage() / 100f
                currentPositionPercentage = (getCurrentPosition() / getDuration().toFloat())

                if (duration == 0L) {
                    if (getDuration() > 0L) {
                        duration = getDuration()
                    }
                }
            }

            delay(DELAY_INTERVAL)
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.Start
    ) {
        TimeStampText(
            modifier = timeStampModifier,
            currentPositionProvider = { currentPosition },
            durationProvider = { duration },
            onExpandVideo = onExpandVideo
        )

        // TODO: Seeker  Implementation
    }
}

@Composable
fun TimeStampText(
    modifier: Modifier = Modifier,
    currentPositionProvider: () -> Long,
    durationProvider: () -> Long,
    onExpandVideo: () -> Unit
) {
    Row(modifier = modifier) {
        FrequentlyChangingText(
            currentPositionProvider = { currentPositionProvider().millisToPlayerDurationFormat() },
            durationProvider = { durationProvider().millisToPlayerDurationFormat() }
        )

        Spacer(Modifier.weight(1f))

        CustomIconButton(onClick = onExpandVideo) {
            Icon(
                painter = painterResource(R.drawable.ic_expand),
                modifier = Modifier.size(16.dp),
                contentDescription = null,
                tint = TrainingMobileTheme.colorScheme.textPrimary
            )
        }
    }
}

@Composable
private fun FrequentlyChangingText(
    modifier: Modifier = Modifier,
    currentPositionProvider: () -> String,
    durationProvider: () -> String
) {

    Text(
        modifier = modifier,
        text = stringResource(
            R.string.format_time_stamp,
            currentPositionProvider(), durationProvider()
        ),
        style = TrainingMobileTheme.typography.medium.copy(
            fontSize = 14.sp,
            color = TrainingMobileTheme.colorScheme.textPrimary
        )
    )
}

@Preview
@Composable
private fun TimeStampTextPreview() {
    TrainingMobileTheme {
        TimeStampText(
            currentPositionProvider = { 0L },
            durationProvider = { 0L }
        ) { }
    }
}
