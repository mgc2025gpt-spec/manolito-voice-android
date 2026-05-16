package com.manolito.voice.feature.home.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manolito.voice.core.audio.model.RecordedAudio
import com.manolito.voice.core.model.conversation.ConversationRequest
import com.manolito.voice.core.model.voice.VoiceUiState
import com.manolito.voice.core.network.api.ConversationApi
import kotlinx.coroutines.launch

class HomeViewModel(
    private val conversationApi: ConversationApi = ConversationApi(),
) : ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    fun onIdleTapRequestPermission() {
        uiState = uiState.copy(
            voiceState = VoiceUiState.Idle,
            transcript = "Necesito permiso de micrófono para empezar.",
        )
    }

    fun onRecordingStarted() {
        uiState = uiState.copy(
            voiceState = VoiceUiState.Listening,
            transcript = "Grabando… habla con naturalidad.",
            serverTranscript = null,
            isRecording = true,
            isPlayingReplyAudio = false,
        )
    }

    fun onRecordingFailed() {
        uiState = uiState.copy(
            voiceState = VoiceUiState.Error,
            transcript = "No pude iniciar la grabación.",
            isRecording = false,
        )
    }

    fun onRecordingFinished(audio: RecordedAudio) {
        uiState = uiState.copy(
            voiceState = VoiceUiState.Processing,
            transcript = "Audio capturado. Subiendo al backend…",
            isRecording = false,
            lastRecordingPath = audio.filePath,
            lastRecordingDurationMs = audio.durationHintMs,
        )
        sendRecordedAudio(audio)
    }

    fun onPrimaryActionReset() {
        uiState = uiState.copy(
            voiceState = VoiceUiState.Idle,
            transcript = "Pulsa para empezar a hablar.",
            serverTranscript = null,
            isRecording = false,
            lastReplyAudioUrl = null,
            isPlayingReplyAudio = false,
        )
    }

    fun sendTextProbe() {
        viewModelScope.launch {
            val result = conversationApi.sendMessage(
                ConversationRequest(
                    text = "Hola, esta es una prueba inicial desde Android.",
                    conversationId = uiState.conversationId,
                ),
            )

            uiState = result.fold(
                onSuccess = { response ->
                    uiState.copy(
                        voiceState = VoiceUiState.Speaking,
                        transcript = response.reply.text,
                        serverTranscript = response.transcript,
                        conversationId = response.conversationId,
                        lastReplyAudioUrl = response.reply.audioUrl,
                        isPlayingReplyAudio = false,
                    )
                },
                onFailure = {
                    uiState.copy(
                        voiceState = VoiceUiState.Error,
                        transcript = "No pude conectar con el backend todavía.",
                    )
                },
            )
        }
    }

    private fun sendRecordedAudio(audio: RecordedAudio) {
        viewModelScope.launch {
            val result = conversationApi.sendAudio(
                filePath = audio.filePath,
                conversationId = uiState.conversationId,
            )

            uiState = result.fold(
                onSuccess = { response ->
                    uiState.copy(
                        voiceState = VoiceUiState.Speaking,
                        transcript = response.reply.text,
                        serverTranscript = response.transcript,
                        conversationId = response.conversationId,
                        lastReplyAudioUrl = response.reply.audioUrl,
                        isPlayingReplyAudio = false,
                    )
                },
                onFailure = {
                    uiState.copy(
                        voiceState = VoiceUiState.Error,
                        transcript = "Grabé el audio, pero no pude subirlo al backend todavía.",
                    )
                },
            )
        }
    }

    fun onReplyAudioPlaybackChanged(isPlaying: Boolean) {
        uiState = uiState.copy(isPlayingReplyAudio = isPlaying)
    }

    fun canReplayAudio(): Boolean = !uiState.lastReplyAudioUrl.isNullOrBlank()
}
