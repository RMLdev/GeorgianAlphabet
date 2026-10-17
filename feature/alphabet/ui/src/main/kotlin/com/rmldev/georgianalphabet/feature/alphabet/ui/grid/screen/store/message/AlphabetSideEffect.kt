package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message

internal sealed interface AlphabetSideEffect {

    data class OpenGlyph(val id: Int) : AlphabetSideEffect
}