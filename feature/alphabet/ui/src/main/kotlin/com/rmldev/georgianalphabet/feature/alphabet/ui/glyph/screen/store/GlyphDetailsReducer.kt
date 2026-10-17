package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store

import com.rmldev.georgianalphabet.core.ui.MviReducer
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsEvent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.GlyphDetailsState
import javax.inject.Inject

internal class GlyphDetailsReducer @Inject constructor() :
    MviReducer<GlyphDetailsState, GlyphDetailsEvent> {

    override fun reduce(
        oldState: GlyphDetailsState,
        event: GlyphDetailsEvent
    ): GlyphDetailsState {
        return GlyphDetailsState()
    }
}
