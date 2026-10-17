package com.rmldev.georgianalphabet.core.audio

/** One started sound. */
internal interface Playback {

    /** Stops the sound and frees its resources. [PlaybackFactory.start]'s `onFinished` is not called after this. */
    fun release()
}

internal interface PlaybackFactory {

    /**
     * Opens [assetPath] and starts playing it.
     *
     * @param onFinished called once, on any thread, when the sound ends by itself or fails while playing.
     * @throws com.rmldev.georgianalphabet.core.audio.api.SoundNotFoundException if there is no such asset.
     */
    fun start(assetPath: String, onFinished: () -> Unit): Playback
}

internal interface AudioFocus {

    /** @return `false` if the system refused focus (e.g. during a call). */
    fun request(onLoss: () -> Unit): Boolean

    fun abandon()
}
