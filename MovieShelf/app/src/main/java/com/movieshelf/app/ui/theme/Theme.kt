package com.movieshelf.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/** Jasny schemat kolorów — zgodny z makietą (bez dark mode na razie) */
private val LightColors = lightColorScheme(
    primary = MsInk,
    onPrimary = MsBgElevated,
    secondary = MsAmber,
    onSecondary = MsBgElevated,
    tertiary = MsTeal,
    background = MsBg,
    onBackground = MsInk,
    surface = MsBgElevated,
    onSurface = MsInk,
    surfaceVariant = MsBg,
    onSurfaceVariant = MsInkSoft,
    outline = MsLine,
    error = MsDanger
)

@Composable
fun MovieShelfTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = MovieShelfTypography,
        content = content
    )
}
