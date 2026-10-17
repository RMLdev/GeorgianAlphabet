package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen

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
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = GlyphDetailsViewModel.Factory::class)
internal class GlyphDetailsViewModel @AssistedInject constructor(
    @Assisted glyphId: Int,
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
    initialState = GlyphDetailsState(glyphId),
    initialActions = listOf(GlyphDetailsAction.LoadLetter),
    uiStateMapper = uiStateMapper,
    intentToAction = { intent ->
        when (intent) {
            GlyphDetailsIntent.Stub -> GlyphDetailsAction.Stub
        }
    },
) {

    @AssistedFactory
    interface Factory {
        fun create(glyphId: Int): GlyphDetailsViewModel
    }
}
