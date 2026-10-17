package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen

import com.rmldev.georgianalphabet.core.ui.StoreViewModel
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.AlphabetActor
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.AlphabetReducer
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetAction
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetEvent
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetIntent
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetSideEffect
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetState
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetStateToUiMapper
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class AlphabetViewModel @Inject constructor(
    actor: AlphabetActor,
    reducer: AlphabetReducer,
    uiStateMapper: AlphabetStateToUiMapper,
) : StoreViewModel<AlphabetIntent, AlphabetAction, AlphabetState, AlphabetUiState, AlphabetSideEffect, AlphabetEvent>(
    actor = actor,
    reducer = reducer,
    initialState = AlphabetState(),
    // ObserveLetters never completes, so it must stay the last initial action
    initialActions = listOf(AlphabetAction.ObserveLetters),
    uiStateMapper = uiStateMapper,
    intentToAction = { intent ->
        when (intent) {
            is AlphabetIntent.GlyphClicked -> AlphabetAction.OpenGlyph(intent.id)
            is AlphabetIntent.FilterSelected -> AlphabetAction.SelectFilter(intent.filter)
            is AlphabetIntent.SortSelected -> AlphabetAction.SelectSort(intent.sort)
        }
    },
)
