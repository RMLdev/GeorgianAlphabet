package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store

import com.rmldev.georgianalphabet.core.ui.MviReducer
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetEvent
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetState
import javax.inject.Inject

internal class AlphabetReducer @Inject constructor() : MviReducer<AlphabetState, AlphabetEvent> {

    override fun reduce(oldState: AlphabetState, event: AlphabetEvent): AlphabetState = when (event) {
        is AlphabetEvent.LettersUpdated -> oldState.copy(
            letters = event.letters,
            progress = event.progress,
            knownScript = event.knownScript,
        )

        is AlphabetEvent.FontShuffleChanged -> oldState.copy(fontShuffleSeed = event.seed)
    }
}
