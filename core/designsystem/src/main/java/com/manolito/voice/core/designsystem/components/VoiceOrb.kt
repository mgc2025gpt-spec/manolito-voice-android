package com.manolito.voice.core.designsystem.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.manolito.voice.core.model.voice.VoiceUiState

@Composable
fun VoiceOrb(state: VoiceUiState, modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "voice_orb")
    val scale = transition.animateFloat(
        initialValue = 0.96f,
        targetValue = if (state == VoiceUiState.Listening) 1.08f else 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "orb_scale",
    )

    val primary = when (state) {
        VoiceUiState.Listening -> MaterialTheme.colorScheme.secondary
        VoiceUiState.Speaking -> MaterialTheme.colorScheme.tertiary
        VoiceUiState.Processing -> MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        VoiceUiState.Error -> MaterialTheme.colorScheme.error
        VoiceUiState.Idle -> MaterialTheme.colorScheme.primary
    }

    Box(
        modifier = modifier
            .size(220.dp)
            .scale(scale.value)
            .border(
                width = 1.dp,
                color = primary.copy(alpha = 0.35f),
                shape = CircleShape,
            )
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(primary, primary.copy(alpha = 0.25f), MaterialTheme.colorScheme.background),
                ),
                shape = CircleShape,
            ),
    )
}
