package com.rmldev.georgianalphabet.core.audio

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.rmldev.georgianalphabet.core.audio.api.SoundNotFoundException
import java.io.IOException

internal val speechAudioAttributes: AudioAttributes = AudioAttributes.Builder()
    .setUsage(AudioAttributes.USAGE_MEDIA)
    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
    .build()

/** Plays assets with [MediaPlayer]; mp3 is stored uncompressed in the APK, so `openFd` works. */
internal class MediaPlayerPlaybackFactory(private val assets: AssetManager) : PlaybackFactory {

    // Any failure of MediaPlayer must release it before reaching SoundPlayerImpl
    @Suppress("TooGenericExceptionCaught")
    override fun start(assetPath: String, onFinished: () -> Unit): Playback {
        val descriptor = openOrPlaceholder(assetPath)
        val player = MediaPlayer()
        try {
            descriptor.use { player.setDataSource(it.fileDescriptor, it.startOffset, it.length) }
            player.setAudioAttributes(speechAudioAttributes)
            player.setOnCompletionListener { onFinished() }
            player.setOnErrorListener { _, _, _ ->
                onFinished()
                true
            }
            player.prepare()
            player.start()
        } catch (e: Exception) {
            player.release()
            throw e
        }
        return MediaPlayerPlayback(player)
    }

    private fun openOrPlaceholder(assetPath: String): AssetFileDescriptor = try {
        assets.openFd(assetPath)
    } catch (e: IOException) {
        val placeholder = PLACEHOLDER_SOUND_PATH ?: throw SoundNotFoundException(assetPath, e)
        try {
            assets.openFd(placeholder)
        } catch (_: IOException) {
            throw SoundNotFoundException(assetPath, e)
        }
    }
}

private class MediaPlayerPlayback(private val player: MediaPlayer) : Playback {

    override fun release() {
        player.setOnCompletionListener(null)
        player.setOnErrorListener(null)
        player.release()
    }
}
