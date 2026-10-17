package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.rmldev.georgianalphabet.core.ui.StoreViewModel
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.GlyphDetailsActor
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.GlyphDetailsReducer
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsAction
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsEvent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsIntent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsSideEffect
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.GlyphDetailsState
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.GlyphDetailsStateToUiMapper
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.GlyphDetailsUiState
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.restoreFontId
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.saveFontId
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = GlyphDetailsViewModel.Factory::class)
internal class GlyphDetailsViewModel @AssistedInject constructor(
    @Assisted glyphId: Int,
    @Assisted fontId: String?,
    savedStateHandle: SavedStateHandle,
    actor: GlyphDetailsActor,
    reducer: GlyphDetailsReducer,
    uiStateMapper: GlyphDetailsStateToUiMapper,
) : StoreViewModel<
    GlyphDetailsIntent,
    GlyphDetailsAction,
    GlyphDetailsState,
    GlyphDetailsUiState,
    GlyphDetailsSideEffect,
    GlyphDetailsEvent,
    >(
    actor = actor,
    reducer = reducer,
    initialState = GlyphDetailsState(glyphId, currentFontId = savedStateHandle.restoreFontId(fontId)),
    // ObserveProgress never completes, so it must stay the last initial action
    initialActions = listOf(GlyphDetailsAction.LoadLetter, GlyphDetailsAction.ObserveProgress),
    uiStateMapper = uiStateMapper,
    intentToAction = { it.toAction() },
) {

    init {
        viewModelScope.launch {
            state.map { it.currentFontId }.distinctUntilChanged().collect { savedStateHandle.saveFontId(it) }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(glyphId: Int, fontId: String?): GlyphDetailsViewModel
    }
}

private fun GlyphDetailsIntent.toAction(): GlyphDetailsAction = when (this) {
    is GlyphDetailsIntent.FontSelected -> GlyphDetailsAction.SelectFont(fontId)
    GlyphDetailsIntent.PlayName -> GlyphDetailsAction.PlayName
    is GlyphDetailsIntent.PlayWord -> GlyphDetailsAction.PlayWord(georgian)
    is GlyphDetailsIntent.SimilarClicked -> GlyphDetailsAction.OpenLetter(letterId)
    GlyphDetailsIntent.AllFontsClicked -> GlyphDetailsAction.OpenAllFonts
    GlyphDetailsIntent.BackClicked -> GlyphDetailsAction.Back
}
