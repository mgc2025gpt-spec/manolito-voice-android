package com.manolito.voice.core.audio.player

interface AudioPlayer {
    fun play(url: String)
    fun stop()
    fun release()
    fun isPlaying(): Boolean
}
