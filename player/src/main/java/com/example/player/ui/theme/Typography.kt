package com.example.player.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import com.example.player.R

internal val LocalTypographyComposition = staticCompositionLocalOf { Typography() }

abstract class BaseTypography {
    abstract val light: TextStyle
    abstract val normal: TextStyle
    abstract val medium: TextStyle
    abstract val semiBold: TextStyle
    abstract val bold: TextStyle
    abstract val extraBold: TextStyle
}

object Fonts {
    val light: Font = Font(R.font.roboto_light)
    val regular: Font = Font(R.font.roboto_regular)
    val medium: Font = Font(R.font.roboto_medium)
    val semiBold: Font = Font(R.font.roboto_semibold)
    val bold: Font = Font(R.font.roboto_bold)
    val extraBold: Font = Font(R.font.roboto_extra_bold)
}

@Immutable
data class Typography(
    override val light: TextStyle = TextStyle(
        fontFamily = Fonts.light.toFontFamily(),
        fontWeight = FontWeight.Light
    ),
    override val normal: TextStyle = TextStyle(
        fontFamily = Fonts.regular.toFontFamily(),
        fontWeight = FontWeight.Normal
    ),
    override val medium: TextStyle = TextStyle(
        fontFamily = Fonts.medium.toFontFamily(),
        fontWeight = FontWeight.Medium
    ),
    override val semiBold: TextStyle = TextStyle(
        fontFamily = Fonts.semiBold.toFontFamily(),
        fontWeight = FontWeight.SemiBold
    ),
    override val bold: TextStyle = TextStyle(
        fontFamily = Fonts.bold.toFontFamily(),
        fontWeight = FontWeight.Bold
    ),
    override val extraBold: TextStyle = TextStyle(
        fontFamily = Fonts.extraBold.toFontFamily(),
        fontWeight = FontWeight.ExtraBold
    )
) : BaseTypography()
