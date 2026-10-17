package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message

import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter

internal sealed interface AlphabetEvent {
    data class LettersLoaded(val letters: List<Letter>) : AlphabetEvent
}
