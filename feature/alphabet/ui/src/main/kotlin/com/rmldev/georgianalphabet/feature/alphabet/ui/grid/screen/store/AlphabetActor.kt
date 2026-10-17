package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store

import com.rmldev.georgianalphabet.core.ui.ActorScope
import com.rmldev.georgianalphabet.core.ui.MviActor
import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.GetLettersUseCase
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetAction
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetEvent
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetSideEffect
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetState
import javax.inject.Inject

private typealias AlphabetActorScope = ActorScope<AlphabetState, AlphabetEvent, AlphabetSideEffect>

internal class AlphabetActor @Inject constructor(private val getLetters: GetLettersUseCase) :
    MviActor<AlphabetAction, AlphabetState, AlphabetEvent, AlphabetSideEffect> {

    override suspend fun AlphabetActorScope.executeAction(action: AlphabetAction) {
        when (action) {
            AlphabetAction.Stub -> Unit

            AlphabetAction.LoadLetters -> event(
                AlphabetEvent.LettersLoaded(getLetters().filterNot { it.archaic }.sortedBy { it.order }),
            )

            is AlphabetAction.OpenGlyph -> sideEffect(AlphabetSideEffect.OpenGlyph(action.id))
        }
    }
}
