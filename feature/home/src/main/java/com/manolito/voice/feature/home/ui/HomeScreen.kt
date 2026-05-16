package com.manolito.voice.feature.home.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.matchParentSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.manolito.voice.core.designsystem.components.PrimaryVoiceButton
import com.manolito.voice.core.designsystem.components.VoiceOrb
import com.manolito.voice.core.designsystem.theme.AccentListening
import com.manolito.voice.core.designsystem.theme.AccentPrimary
import com.manolito.voice.core.designsystem.theme.Background
import com.manolito.voice.core.designsystem.theme.SurfaceGlass
import com.manolito.voice.feature.home.presentation.HomeUiState

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onPrimaryAction: () -> Unit,
    onReplayAudio: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(AccentPrimary.copy(alpha = 0.18f), Background),
                    ),
                )
                .alpha(0.9f),
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(AccentListening.copy(alpha = 0.10f), Background, Background),
                    ),
                )
                .alpha(0.65f),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.58f),
                shape = RoundedCornerShape(999.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.8f)),
            ) {
                Text(
                    text = uiState.statusLabel.uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Manolito Voice",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Una presencia de voz limpia, cálida y muy tuya.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(34.dp))
            VoiceOrb(state = uiState.voiceState)
            Spacer(modifier = Modifier.height(28.dp))
            Surface(
                color = SurfaceGlass,
                shape = RoundedCornerShape(30.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.75f)),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Respuesta actual",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = uiState.transcript,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    if (uiState.serverTranscript != null) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Transcripción backend",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = uiState.serverTranscript,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    if (uiState.lastReplyAudioUrl != null) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = if (uiState.isPlayingReplyAudio) "Audio de voz reproduciéndose" else "Audio de voz listo",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(22.dp))
            PrimaryVoiceButton(
                text = when {
                    uiState.isRecording -> "Detener grabación"
                    uiState.statusLabel == "Procesando" -> "Procesando"
                    uiState.statusLabel == "Respuesta lista" -> "Reiniciar"
                    else -> "Hablar ahora"
                },
                onClick = onPrimaryAction,
            )
            if (uiState.lastReplyAudioUrl != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    OutlinedButton(
                        onClick = onReplayAudio,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.85f)),
                    ) {
                        Text(if (uiState.isPlayingReplyAudio) "Reiniciar audio" else "Reproducir otra vez")
                    }
                }
            }
            if (uiState.lastRecordingPath != null) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Audio local guardado · ${uiState.lastRecordingDurationMs?.let { "${it}ms" } ?: "duración pendiente"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.55f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                )
            }
        }
    }
}
