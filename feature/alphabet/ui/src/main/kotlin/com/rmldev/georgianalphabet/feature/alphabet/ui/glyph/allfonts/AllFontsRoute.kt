package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rmldev.georgianalphabet.core.ui.CollectSideEffects
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.AllFontsViewModel
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.message.AllFontsSideEffect
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.ui.AllFontsScreen

/**
 * «Все шрифты» буквы [letterId].
 *
 * @param onOpenFont вернуться на карточку буквы и показать её в выбранном шрифте: (letterId, fontId).
 */
@Composable
internal fun AllFontsRoute(
    letterId: Int,
    onBack: () -> Unit,
    onOpenFont: (Int, String) -> Unit,
    viewModel: AllFontsViewModel = hiltViewModel<AllFontsViewModel, AllFontsViewModel.Factory>(
        creationCallback = { factory -> factory.create(letterId) },
    ),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CollectSideEffects(viewModel.sideEffects) { sideEffect ->
        when (sideEffect) {
            is AllFontsSideEffect.OpenFont -> onOpenFont(sideEffect.letterId, sideEffect.fontId)
            AllFontsSideEffect.Back -> onBack()
        }
    }

    AllFontsScreen(
        state = state,
        onIntent = viewModel::dispatch,
    )
}
