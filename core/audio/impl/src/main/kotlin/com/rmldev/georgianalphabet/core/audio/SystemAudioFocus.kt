package com.rmldev.georgianalphabet.core.audio

import android.media.AudioFocusRequest
import android.media.AudioManager

/** Short transient focus: other apps duck while a sound plays and get focus back right after. */
internal class SystemAudioFocus(private val audioManager: AudioManager) : AudioFocus {

    private var request: AudioFocusRequest? = null

    override fun request(onLoss: () -> Unit): Boolean {
        abandon()
        val newRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
            .setAudioAttributes(speechAudioAttributes)
            .setOnAudioFocusChangeListener { change ->
                if (change < 0) onLoss()
            }
            .build()
        val granted = audioManager.requestAudioFocus(newRequest) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        if (granted) request = newRequest
        return granted
    }

    override fun abandon() {
        request?.let(audioManager::abandonAudioFocusRequest)
        request = null
    }
}
