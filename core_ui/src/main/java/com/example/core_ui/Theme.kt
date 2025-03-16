package com.example.core_ui

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun TrainingMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) DarkColors() else LightColors()
        }

        darkTheme -> DarkColors()
        else -> LightColors()
    }
    val typography = Typography()
    CompositionLocalProvider(
        LocalColorComposition provides colorScheme,
        LocalTypographyComposition provides typography
    ) {
        MaterialTheme(
            content = content
        )
    }
}

object TrainingMobileTheme {
    val colorScheme: BaseColors
        @Composable
        get() = LocalColorComposition.current

    val typography: Typography
        @Composable
        get() = LocalTypographyComposition.current
}
