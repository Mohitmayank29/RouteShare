package com.mohit.mapsone.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = White,
    primaryContainer = DarkPrimaryDark,
    onPrimaryContainer = White,

    secondary = DarkSecondary,
    onSecondary = Black,
    secondaryContainer = DarkPrimaryDark,
    onSecondaryContainer = White,

    tertiary = DarkPrimary,
    onTertiary = Black,

    background = DarkBackground,
    onBackground = DarkTextPrimary,

    surface = DarkSurface,
    onSurface = DarkTextPrimary,

    surfaceVariant = DarkCard,
    onSurfaceVariant = DarkTextSecondary,

    error = Error,
    onError = White,

    outline = DarkTextSecondary,
    outlineVariant = DarkCard
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = Black,
    primaryContainer = LightPrimaryDark,
    onPrimaryContainer = White,

    secondary = LightSecondary,
    onSecondary = Black,
    secondaryContainer = LightPrimaryDark,
    onSecondaryContainer = White,

    tertiary = LightPrimary,
    onTertiary = Black,

    background = LightBackground,
    onBackground = LightTextPrimary,

    surface = LightSurface,
    onSurface = LightTextPrimary,

    surfaceVariant = LightCard,
    onSurfaceVariant = LightTextSecondary,

    error = Error,
    onError = White,

    outline = LightTextSecondary,
    outlineVariant = LightCard
)

@Composable
fun MapsoneTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}