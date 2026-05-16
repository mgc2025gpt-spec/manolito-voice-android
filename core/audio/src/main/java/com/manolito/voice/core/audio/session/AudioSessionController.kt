package com.manolito.voice.core.audio.session

import com.manolito.voice.core.audio.model.RecordedAudio
import com.manolito.voice.core.audio.recorder.AudioRecorder

class AudioSessionController(
    private val recorder: AudioRecorder,
) {
    fun startRecording(): Result<Unit> = recorder.start()
    fun stopRecording(): Result<RecordedAudio> = recorder.stop()
    fun cancelRecording() = recorder.cancel()
    fun isRecording(): Boolean = recorder.isRecording()
}
