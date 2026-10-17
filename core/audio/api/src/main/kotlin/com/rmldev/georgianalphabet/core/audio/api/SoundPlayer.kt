package com.rmldev.georgianalphabet.core.audio.api

import kotlinx.coroutines.flow.StateFlow

/**
 * Plays short sounds bundled in the app assets.
 *
 * Asset paths are relative to `assets/` and follow [AudioAssets]:
 * - `audio/letters/<letterId>.mp3` — name of the letter;
 * - `audio/words/<slug>.mp3` — example word.
 *
 * Only one sound plays at a time: a new [play] interrupts the previous one.
 * Playback uses the media stream, so it follows the system media volume, and audio focus
 * is released as soon as the sound ends or is stopped.
 */
interface SoundPlayer {

    /** Asset path of the sound that is playing now, or `null` when silent. */
    val playing: StateFlow<String?>

    /**
     * Stops the current sound and starts [assetPath]. Returns once playback has started,
     * without waiting for it to finish.
     *
     * Never throws on a playback problem: a missing file fails with [SoundNotFoundException],
     * other problems (audio focus denied, broken file) fail with their own exception.
     */
    suspend fun play(assetPath: String): Result<Unit>

    /** Stops the current sound, if any. */
    fun stop()
}

class SoundNotFoundException(val assetPath: String, cause: Throwable? = null) :
    Exception("Sound asset not found: $assetPath", cause)
