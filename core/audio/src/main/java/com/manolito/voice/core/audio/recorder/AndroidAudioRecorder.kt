package com.manolito.voice.core.audio.recorder

import android.content.Context
import android.media.MediaRecorder
import android.os.SystemClock
import com.manolito.voice.core.audio.model.RecordedAudio
import java.io.File

class AndroidAudioRecorder(
    private val context: Context,
) : AudioRecorder {
    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null
    private var startedAtMs: Long = 0L

    override fun start(): Result<Unit> = runCatching {
        if (recorder != null) return@runCatching

        val file = File(context.cacheDir, "recording-${System.currentTimeMillis()}.m4a")
        val mediaRecorder = MediaRecorder(context).apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(128_000)
            setAudioSamplingRate(44_100)
            setOutputFile(file.absolutePath)
            prepare()
            start()
        }

        outputFile = file
        recorder = mediaRecorder
        startedAtMs = SystemClock.elapsedRealtime()
    }

    override fun stop(): Result<RecordedAudio> = runCatching {
        val mediaRecorder = recorder ?: error("Recorder not started")
        val file = outputFile ?: error("No output file")

        mediaRecorder.stop()
        mediaRecorder.reset()
        mediaRecorder.release()
        recorder = null

        val duration = (SystemClock.elapsedRealtime() - startedAtMs).coerceAtLeast(0L)
        startedAtMs = 0L
        outputFile = null

        RecordedAudio(
            filePath = file.absolutePath,
            durationHintMs = duration,
        )
    }

    override fun cancel() {
        runCatching { recorder?.stop() }
        runCatching { recorder?.reset() }
        runCatching { recorder?.release() }
        recorder = null
        startedAtMs = 0L
        outputFile?.delete()
        outputFile = null
    }

    override fun isRecording(): Boolean = recorder != null
}
