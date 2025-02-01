package com.medioka.player.custom.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role

@Composable
fun CustomIconButton(
    modifier: Modifier = Modifier, onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            enabled = true,
            role = Role.Button,
            indication = ripple(bounded = false),
            onClick = onClick
        ),
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}