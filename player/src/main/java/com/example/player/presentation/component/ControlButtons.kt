package com.example.player.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.player.R
import com.example.player.presentation.custom.button.CustomIconButton
import com.example.player.ui.theme.TrainingMobileTheme

@Composable
fun ControlButtons(
    modifier: Modifier = Modifier,
    isPlayingProvider: () -> Boolean,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(42.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NormalButton(
            iconRes = R.drawable.ic_previous,
            onClick = onPrevious
        )

        PlayAndPauseButton(
            isPlayingProvider = isPlayingProvider,
            onPause = onPause,
            onPlay = onPlay
        )

        NormalButton(
            iconRes = R.drawable.ic_next,
            onClick = onNext
        )
    }
}

@Composable
private fun PlayAndPauseButton(
    isPlayingProvider: () -> Boolean,
    onPause: () -> Unit,
    onPlay: () -> Unit
) {
    CustomIconButton(
        modifier = Modifier
            .clip(RoundedCornerShape(27.dp))
            .background(TrainingMobileTheme.colorScheme.playerButtonBackground)
            .padding(13.dp),
        onClick = {
            if (isPlayingProvider()) onPause() else onPlay()
        }
    ) {
        AnimatedContent(targetState = isPlayingProvider()) { isPlaying ->
            if (isPlaying) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(R.drawable.ic_pause),
                    tint = TrainingMobileTheme.colorScheme.textPrimary,
                    contentDescription = null
                )
            } else {
                Icon(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(R.drawable.ic_play),
                    tint = TrainingMobileTheme.colorScheme.textPrimary,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun NormalButton(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit
) {
    CustomIconButton(
        modifier = modifier
            .clip(RoundedCornerShape(27.dp))
            .background(TrainingMobileTheme.colorScheme.playerButtonBackground)
            .padding(13.dp),
        onClick = onClick,
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            tint = TrainingMobileTheme.colorScheme.textPrimary,
            painter = painterResource(iconRes),
            contentDescription = null
        )
    }
}

@Preview(
    backgroundColor = 0xFFFFFFFF, showBackground = true,
    widthDp = 300
)
@Composable
private fun ControlButtonsPreview() {
    TrainingMobileTheme {
        var isPlaying by remember { mutableStateOf(false) }
        ControlButtons(
            isPlayingProvider = { isPlaying },
            onPause = { isPlaying = !isPlaying },
            onPlay = { isPlaying = !isPlaying },
            onNext = {},
            onPrevious = {}
        )
    }
}
