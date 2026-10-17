package com.rmldev.georgianalphabet.core.audio

import com.rmldev.georgianalphabet.core.audio.api.SoundPlayer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

internal class SoundPlayerImpl(
    private val playbackFactory: PlaybackFactory,
    private val audioFocus: AudioFocus,
    private val ioDispatcher: CoroutineDispatcher,
) : SoundPlayer {

    private val lock = Any()
    private val _playing = MutableStateFlow<String?>(null)
    override val playing: StateFlow<String?> = _playing.asStateFlow()

    // Guarded by lock. The token tells a finished sound from the one that replaced it.
    private var current: Playback? = null
    private var currentToken: Any? = null

    // Playback problems are returned as Result, never thrown to the caller
    @Suppress("TooGenericExceptionCaught")
    override suspend fun play(assetPath: String): Result<Unit> = withContext(ioDispatcher) {
        synchronized(lock) {
            releaseCurrent()
            if (!audioFocus.request(onLoss = ::stop)) {
                return@withContext Result.failure(AudioFocusDeniedException())
            }
            val token = Any()
            currentToken = token
            try {
                current = playbackFactory.start(assetPath) { onFinished(token) }
            } catch (e: Exception) {
                currentToken = null
                audioFocus.abandon()
                return@withContext Result.failure(e)
            }
            _playing.value = assetPath
            Result.success(Unit)
        }
    }

    override fun stop() {
        synchronized(lock) {
            if (releaseCurrent()) audioFocus.abandon()
        }
    }

    private fun onFinished(token: Any) {
        synchronized(lock) {
            if (currentToken === token) {
                releaseCurrent()
                audioFocus.abandon()
            }
        }
    }

    /** @return `true` if something was playing. */
    private fun releaseCurrent(): Boolean {
        val playback = current ?: return false
        current = null
        currentToken = null
        _playing.value = null
        playback.release()
        return true
    }
}

internal class AudioFocusDeniedException : Exception("Audio focus denied")
