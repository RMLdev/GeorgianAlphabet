package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.GlyphDetailsViewModel
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.GlyphDetailsScreen

@Composable
internal fun GlyphDetailsRoute(
    glyphId: Int,
    viewModel: GlyphDetailsViewModel = hiltViewModel<GlyphDetailsViewModel, GlyphDetailsViewModel.Factory>(
        creationCallback = { factory -> factory.create(glyphId) },
    ),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    GlyphDetailsScreen(
        state = state,
        onIntent = viewModel::dispatch
    )
}
