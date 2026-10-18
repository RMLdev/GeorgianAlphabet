package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen

import com.rmldev.georgianalphabet.core.ui.StoreViewModel
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.AllFontsActor
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.AllFontsReducer
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.message.AllFontsAction
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.message.AllFontsEvent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.message.AllFontsIntent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.message.AllFontsSideEffect
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.state.AllFontsState
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.state.AllFontsStateToUiMapper
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.state.AllFontsUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = AllFontsViewModel.Factory::class)
internal class AllFontsViewModel @AssistedInject constructor(
    @Assisted letterId: Int,
    actor: AllFontsActor,
    reducer: AllFontsReducer,
    uiStateMapper: AllFontsStateToUiMapper,
) : StoreViewModel<
    AllFontsIntent,
    AllFontsAction,
    AllFontsState,
    AllFontsUiState,
    AllFontsSideEffect,
    AllFontsEvent,
    >(
    actor = actor,
    reducer = reducer,
    initialState = AllFontsState(letterId),
    // ObserveProgress never completes, so it must stay the last initial action
    initialActions = listOf(AllFontsAction.LoadLetter, AllFontsAction.ObserveProgress),
    uiStateMapper = uiStateMapper,
    intentToAction = { it.toAction() },
) {

    @AssistedFactory
    interface Factory {
        fun create(letterId: Int): AllFontsViewModel
    }
}

private fun AllFontsIntent.toAction(): AllFontsAction = when (this) {
    is AllFontsIntent.FontClicked -> AllFontsAction.OpenFont(fontId)
    AllFontsIntent.BackClicked -> AllFontsAction.Back
}
