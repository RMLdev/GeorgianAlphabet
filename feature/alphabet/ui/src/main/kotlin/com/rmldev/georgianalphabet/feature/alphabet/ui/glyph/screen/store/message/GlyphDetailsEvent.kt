package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message

import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter

internal sealed interface GlyphDetailsEvent {
    data class LetterLoaded(val letter: Letter?) : GlyphDetailsEvent
}
