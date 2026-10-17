package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store

import com.rmldev.georgianalphabet.core.ui.ActorScope
import com.rmldev.georgianalphabet.core.ui.MviActor
import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.GetLettersUseCase
import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.ObserveLettersProgressUseCase
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetAction
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetEvent
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetSideEffect
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetState
import com.rmldev.georgianalphabet.feature.preferences.api.usecase.ObserveUserPreferencesUseCase
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.random.Random

private typealias AlphabetActorScope = ActorScope<AlphabetState, AlphabetEvent, AlphabetSideEffect>

internal class AlphabetActor @Inject constructor(
    private val getLetters: GetLettersUseCase,
    private val observeProgress: ObserveLettersProgressUseCase,
    private val observeUserPreferences: ObserveUserPreferencesUseCase,
) : MviActor<AlphabetAction, AlphabetState, AlphabetEvent, AlphabetSideEffect> {

    override suspend fun AlphabetActorScope.executeAction(action: AlphabetAction) {
        when (action) {
            AlphabetAction.ObserveLetters -> observeLetters()
            is AlphabetAction.OpenGlyph -> sideEffect(AlphabetSideEffect.OpenGlyph(action.id))
            AlphabetAction.ToggleFontShuffle -> toggleFontShuffle()
        }
    }

    private suspend fun AlphabetActorScope.observeLetters() {
        val letters = getLetters().sortedBy { it.order }
        val knownScript = observeUserPreferences().map { it.knownScript }.distinctUntilChanged()
        combine(observeProgress(), knownScript) { progress, script ->
            AlphabetEvent.LettersUpdated(letters, progress, script)
        }.collect { event(it) }
    }

    // A fresh seed on every switch-on keeps the fonts stable until the next press
    private fun AlphabetActorScope.toggleFontShuffle() {
        val seed = if (state.fontShuffleSeed == null) Random.nextLong() else null
        event(AlphabetEvent.FontShuffleChanged(seed))
    }
}
