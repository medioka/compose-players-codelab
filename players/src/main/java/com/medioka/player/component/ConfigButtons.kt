package com.medioka.player.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core_ui.TrainingMobileTheme
import com.medioka.player.custom.button.CustomIconButton
import com.medioka.players.R

@Composable
fun ConfigButtons(
    modifier: Modifier = Modifier,
    isAutoPlayNextProvider: () -> Boolean,
    onAutoPlayNextChange: () -> Unit,
    onSettingClick: () -> Unit,
    onCaptionClick: () -> Unit,
    onCastClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        ConfigButton(iconRes = R.drawable.ic_cast, onClick = onCastClick)

        ConfigButton(iconRes = R.drawable.ic_caption, onClick = onCaptionClick)

        ConfigButton(iconRes = R.drawable.ic_setting, onClick = onSettingClick)
    }
}

@Composable
private fun ConfigButton(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit
) {
    CustomIconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(iconRes),
            tint = TrainingMobileTheme.colorScheme.textPrimary,
            contentDescription = null,
        )
    }
}

@Preview
@Composable
private fun ConfigButtonsPreview() {
    TrainingMobileTheme {
        ConfigButtons(
            isAutoPlayNextProvider = { true },
            onAutoPlayNextChange = {},
            onCaptionClick = {},
            onSettingClick = {},
            onCastClick = {}
        )
    }
}