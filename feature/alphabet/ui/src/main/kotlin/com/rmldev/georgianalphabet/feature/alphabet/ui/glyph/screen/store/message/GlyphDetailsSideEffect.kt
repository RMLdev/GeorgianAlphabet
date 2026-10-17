package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message

internal sealed interface GlyphDetailsSideEffect {
    /** Открыть карточку другой буквы вместо текущей. */
    data class OpenLetter(val letterId: Int) : GlyphDetailsSideEffect

    data class OpenAllFonts(val letterId: Int) : GlyphDetailsSideEffect

    object Back : GlyphDetailsSideEffect
}
