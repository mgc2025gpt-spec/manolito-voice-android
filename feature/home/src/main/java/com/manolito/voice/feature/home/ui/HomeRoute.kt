package com.manolito.voice.feature.home.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.manolito.voice.core.audio.player.StreamAudioPlayer
import com.manolito.voice.core.audio.recorder.AndroidAudioRecorder
import com.manolito.voice.core.audio.session.AudioSessionController
import com.manolito.voice.core.model.voice.VoiceUiState
import com.manolito.voice.core.network.client.NetworkConfig
import com.manolito.voice.feature.home.presentation.HomeViewModel

@Composable
fun HomeRoute(viewModel: HomeViewModel = viewModel()) {
    val context = LocalContext.current
    val audioSessionController = remember(context) {
        AudioSessionController(AndroidAudioRecorder(context))
    }
    val audioPlayer = remember(context) { StreamAudioPlayer(context) }

    fun replayReplyAudio() {
        val audioUrl = viewModel.uiState.lastReplyAudioUrl ?: return
        val resolvedUrl = if (audioUrl.startsWith("http")) audioUrl else NetworkConfig.BASE_URL + audioUrl
        runCatching {
            audioPlayer.play(resolvedUrl)
            viewModel.onReplyAudioPlaybackChanged(true)
        }.onFailure {
            viewModel.onReplyAudioPlaybackChanged(false)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            audioPlayer.release()
        }
    }

    LaunchedEffect(viewModel.uiState.lastReplyAudioUrl) {
        if (viewModel.uiState.lastReplyAudioUrl != null) replayReplyAudio()
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) {
            audioSessionController.startRecording()
                .onSuccess { viewModel.onRecordingStarted() }
                .onFailure { viewModel.onRecordingFailed() }
        } else {
            viewModel.onIdleTapRequestPermission()
        }
    }

    HomeScreen(
        uiState = viewModel.uiState,
        onReplayAudio = {
            if (viewModel.canReplayAudio()) replayReplyAudio()
        },
        onPrimaryAction = {
            when {
                viewModel.uiState.isRecording -> {
                    audioSessionController.stopRecording()
                        .onSuccess { viewModel.onRecordingFinished(it) }
                        .onFailure { viewModel.onRecordingFailed() }
                }
                viewModel.uiState.voiceState == VoiceUiState.Idle -> {
                    val granted = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.RECORD_AUDIO,
                    ) == PackageManager.PERMISSION_GRANTED

                    if (granted) {
                        audioSessionController.startRecording()
                            .onSuccess { viewModel.onRecordingStarted() }
                            .onFailure { viewModel.onRecordingFailed() }
                    } else {
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                }
                viewModel.uiState.voiceState == VoiceUiState.Listening -> {
                    viewModel.sendTextProbe()
                }
                else -> {
                    audioPlayer.stop()
                    viewModel.onReplyAudioPlaybackChanged(false)
                    viewModel.onPrimaryActionReset()
                }
            }
        },
    )
}
