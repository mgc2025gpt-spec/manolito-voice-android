package com.manolito.voice.core.audio.recorder

import com.manolito.voice.core.audio.model.RecordedAudio

interface AudioRecorder {
    fun start(): Result<Unit>
    fun stop(): Result<RecordedAudio>
    fun cancel()
    fun isRecording(): Boolean
}
