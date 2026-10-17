package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message

internal sealed interface AlphabetAction {
    object Stub : AlphabetAction

    object LoadLetters : AlphabetAction

    data class OpenGlyph(val id: Int) : AlphabetAction
}
