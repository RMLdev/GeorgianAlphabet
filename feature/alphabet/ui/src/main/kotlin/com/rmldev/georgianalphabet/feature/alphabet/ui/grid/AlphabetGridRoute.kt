package com.rmldev.georgianalphabet.feature.alphabet.ui.grid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rmldev.georgianalphabet.core.ui.CollectSideEffects
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.AlphabetViewModel
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetSideEffect
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.ui.AlphabetScreen

@Composable
internal fun AlphabetGreedRoute(
    viewModel: AlphabetViewModel = hiltViewModel(),
    onGlyphClick: (Int) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CollectSideEffects(viewModel.sideEffects) { sideEffect ->
        when (sideEffect) {
            is AlphabetSideEffect.OpenGlyph -> onGlyphClick(sideEffect.id)
        }
    }

    AlphabetScreen(
        state = state,
        onIntent = viewModel::dispatch,
    )
}
