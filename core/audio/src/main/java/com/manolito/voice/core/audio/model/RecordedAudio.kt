package com.manolito.voice.core.audio.model

data class RecordedAudio(
    val filePath: String,
    val durationHintMs: Long? = null,
)
