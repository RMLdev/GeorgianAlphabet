package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state

import javax.inject.Inject

internal class GlyphDetailsStateToUiMapper @Inject constructor() : (GlyphDetailsState) -> GlyphDetailsUiState {

    override fun invoke(state: GlyphDetailsState): GlyphDetailsUiState = GlyphDetailsUiState(
        symbol = state.letter?.glyph?.toString().orEmpty(),
        name = state.letter?.nameLatin.orEmpty(),
    )
}
