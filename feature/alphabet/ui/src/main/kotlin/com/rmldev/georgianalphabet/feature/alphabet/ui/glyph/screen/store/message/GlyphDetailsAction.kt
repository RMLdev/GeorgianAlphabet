package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message

internal sealed interface GlyphDetailsAction {
    object Stub : GlyphDetailsAction

    object LoadLetter : GlyphDetailsAction
}
