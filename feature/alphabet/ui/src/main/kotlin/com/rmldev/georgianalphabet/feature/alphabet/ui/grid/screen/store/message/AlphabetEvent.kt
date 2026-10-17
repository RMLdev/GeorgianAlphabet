package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message

import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LetterProgress
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript

internal sealed interface AlphabetEvent {
    data class LettersUpdated(
        val letters: List<Letter>,
        val progress: Map<Int, LetterProgress>,
        val knownScript: KnownScript,
    ) : AlphabetEvent

    /** [seed] — новое перемешивание шрифтов плиток; null — все плитки в шрифте по умолчанию. */
    data class FontShuffleChanged(val seed: Long?) : AlphabetEvent
}
