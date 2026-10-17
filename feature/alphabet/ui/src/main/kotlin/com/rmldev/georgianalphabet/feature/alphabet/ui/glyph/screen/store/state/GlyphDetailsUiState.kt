package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state

import androidx.compose.runtime.Stable

@Stable
internal data class GlyphDetailsUiState(
    val symbol: String,
    val name: String,
)
