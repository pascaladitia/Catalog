package com.pascal.catalog.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = OceanDark,
    secondary = Ocean,
    tertiary = Coral,
    background = White,
    surface = SurfaceLight,
    onPrimary = White,
    onSecondary = White,
    onBackground = Ink,
    onSurface = Ink,
)

private val DarkColors = darkColorScheme(
    primary = Ocean,
    secondary = Coral,
    tertiary = White,
    background = Ink,
    surface = ColorTokens.DarkSurface,
    onPrimary = Ink,
    onSecondary = Ink,
    onBackground = White,
    onSurface = White,
)

private object ColorTokens {
    val DarkSurface = androidx.compose.ui.graphics.Color(0xFF182230)
}

@Composable
fun CatalogTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = catalogTypography(),
        content = content,
    )
}
