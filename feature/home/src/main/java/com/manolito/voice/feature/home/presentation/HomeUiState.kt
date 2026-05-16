package com.manolito.voice.feature.home.presentation

import com.manolito.voice.core.model.voice.VoiceUiState

data class HomeUiState(
    val voiceState: VoiceUiState = VoiceUiState.Idle,
    val transcript: String = "Pulsa para empezar a hablar.",
    val serverTranscript: String? = null,
    val isRecording: Boolean = false,
    val lastRecordingPath: String? = null,
    val lastRecordingDurationMs: Long? = null,
    val conversationId: String? = null,
    val lastReplyAudioUrl: String? = null,
    val isPlayingReplyAudio: Boolean = false,
) {
    val statusLabel: String
        get() = when {
            isRecording -> "Grabando"
            isPlayingReplyAudio -> "Reproduciendo"
            voiceState == VoiceUiState.Processing -> "Procesando"
            voiceState == VoiceUiState.Speaking -> "Respuesta lista"
            voiceState == VoiceUiState.Error -> "Error"
            else -> "En espera"
        }
}
