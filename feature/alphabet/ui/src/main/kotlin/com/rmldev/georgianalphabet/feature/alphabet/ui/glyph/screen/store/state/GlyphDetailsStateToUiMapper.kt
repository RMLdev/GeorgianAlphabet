package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state

import javax.inject.Inject

class GlyphDetailsStateToUiMapper @Inject constructor() :
        (GlyphDetailsState) -> GlyphDetailsUiState {

    override fun invoke(state: GlyphDetailsState): GlyphDetailsUiState {
        return GlyphDetailsUiState()
    }
}
