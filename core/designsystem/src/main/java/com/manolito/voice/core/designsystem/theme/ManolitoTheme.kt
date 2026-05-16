package com.manolito.voice.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    background = Background,
    surface = Surface,
    surfaceVariant = SurfaceVariant,
    primary = AccentPrimary,
    secondary = AccentListening,
    tertiary = AccentWarm,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = StrokeSubtle,
)

@Composable
fun ManolitoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography = ManolitoTypography,
        shapes = ManolitoShapes,
        content = content,
    )
}
