package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rmldev.georgianalphabet.core.ui.CollectSideEffects
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.GlyphDetailsViewModel
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsSideEffect
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.GlyphDetailsScreen

@Composable
internal fun GlyphDetailsRoute(
    glyphId: Int,
    fontId: String?,
    onBack: () -> Unit,
    onOpenLetter: (Int) -> Unit,
    onOpenAllFonts: (Int) -> Unit,
    viewModel: GlyphDetailsViewModel = hiltViewModel<GlyphDetailsViewModel, GlyphDetailsViewModel.Factory>(
        creationCallback = { factory -> factory.create(glyphId, fontId) },
    ),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CollectSideEffects(viewModel.sideEffects) { sideEffect ->
        when (sideEffect) {
            is GlyphDetailsSideEffect.OpenLetter -> onOpenLetter(sideEffect.letterId)
            is GlyphDetailsSideEffect.OpenAllFonts -> onOpenAllFonts(sideEffect.letterId)
            GlyphDetailsSideEffect.Back -> onBack()
        }
    }

    GlyphDetailsScreen(
        state = state,
        onIntent = viewModel::dispatch,
    )
}
