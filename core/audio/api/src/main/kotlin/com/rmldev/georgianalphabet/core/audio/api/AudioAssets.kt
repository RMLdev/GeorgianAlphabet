package com.rmldev.georgianalphabet.core.audio.api

/** Paths of bundled sounds inside `assets/`, see [SoundPlayer]. */
object AudioAssets {

    /** Name of the letter: `audio/letters/<letterId>.mp3`. */
    fun letter(letterId: Int): String = "audio/letters/$letterId.mp3"

    /** Example word: `audio/words/<slug>.mp3`. */
    fun word(slug: String): String = "audio/words/$slug.mp3"
}
