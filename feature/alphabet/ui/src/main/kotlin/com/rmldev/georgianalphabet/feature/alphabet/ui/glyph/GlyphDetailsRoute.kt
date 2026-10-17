package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.GlyphDetailsViewModel
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.GlyphDetailsScreen

@Composable
internal fun GlyphDetailsRoute(
    viewModel: GlyphDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    GlyphDetailsScreen(
        state = state,
        onIntent = viewModel::dispatch
    )
}
