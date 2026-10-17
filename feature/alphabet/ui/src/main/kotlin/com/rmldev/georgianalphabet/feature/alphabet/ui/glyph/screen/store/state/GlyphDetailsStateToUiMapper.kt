package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state

import javax.inject.Inject

internal class GlyphDetailsStateToUiMapper @Inject constructor() :
        (GlyphDetailsState) -> GlyphDetailsUiState {

    override fun invoke(state: GlyphDetailsState): GlyphDetailsUiState {
        return GlyphDetailsUiState(
            symbol = state.letter.symbol.toString(),
            name = state.letter.name,
        )
    }
}
