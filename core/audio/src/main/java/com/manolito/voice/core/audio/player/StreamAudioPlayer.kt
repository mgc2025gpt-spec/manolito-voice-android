package com.manolito.voice.core.audio.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class StreamAudioPlayer(
    context: Context,
) : AudioPlayer {
    private val player = ExoPlayer.Builder(context).build()

    override fun play(url: String) {
        player.setMediaItem(MediaItem.fromUri(url))
        player.prepare()
        player.playWhenReady = true
    }

    override fun stop() {
        player.stop()
    }

    override fun release() {
        player.release()
    }

    override fun isPlaying(): Boolean = player.isPlaying
}
