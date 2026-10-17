package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message

internal sealed interface AlphabetIntent {

    object Stub : AlphabetIntent

    data class GlyphClicked(
        val id: Int
    ) : AlphabetIntent
}