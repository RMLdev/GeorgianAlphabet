package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store

import com.rmldev.georgianalphabet.core.ui.ActorScope
import com.rmldev.georgianalphabet.core.ui.MviActor
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetAction
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetEvent
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetSideEffect
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetState
import javax.inject.Inject

private typealias AlphabetActorScope = ActorScope<AlphabetState, AlphabetEvent, AlphabetSideEffect>

internal class AlphabetActor @Inject constructor() :
    MviActor<AlphabetAction, AlphabetState, AlphabetEvent, AlphabetSideEffect> {

    override suspend fun AlphabetActorScope.executeAction(
        action: AlphabetAction
    ) {

    }
}