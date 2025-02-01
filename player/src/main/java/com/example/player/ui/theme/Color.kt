package com.example.player.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalColorComposition = staticCompositionLocalOf<BaseColors> {
    DarkColors()
    LightColors()
}

abstract class BaseColors {
    abstract val primaryBackground: Color
    abstract val primaryButtonBackground: Color
    abstract val textPrimary: Color
    abstract val textSecondary: Color
    abstract val textPrimaryHover: Color
    abstract val textSecondaryHover: Color
    abstract val textLink: Color
}

// TODO: Convert color to Light later (currently still the same as Dark Colors)
data class LightColors(
    override val primaryBackground: Color = Color(0xFF000000),
    override val primaryButtonBackground: Color = Color(0xFF272727),
    override val textPrimary: Color = Color(0xFFFFFFFF),
    override val textSecondary: Color = Color(0x80FFFFFF),
    override val textPrimaryHover: Color = Color(0xFF000000),
    override val textSecondaryHover: Color = Color(0xFFFFFFFF),
    override val textLink: Color = Color(0xFF5CA4F8),
) : BaseColors()

data class DarkColors(
    override val primaryBackground: Color = Color(0xFF000000),
    override val primaryButtonBackground: Color = Color(0xFF272727),
    override val textPrimary: Color = Color(0xFFFFFFFF),
    override val textSecondary: Color = Color(0x80FFFFFF),
    override val textPrimaryHover: Color = Color(0xFF000000),
    override val textSecondaryHover: Color = Color(0xFFFFFFFF),
    override val textLink: Color = Color(0xFF5CA4F8),
) : BaseColors()
