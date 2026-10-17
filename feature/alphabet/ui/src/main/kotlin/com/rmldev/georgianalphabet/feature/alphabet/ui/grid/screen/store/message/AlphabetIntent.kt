package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message

internal sealed interface AlphabetIntent {

    data class GlyphClicked(val id: Int) : AlphabetIntent
}
