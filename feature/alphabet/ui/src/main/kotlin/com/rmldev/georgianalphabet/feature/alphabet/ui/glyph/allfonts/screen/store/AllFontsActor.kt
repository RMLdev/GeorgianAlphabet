package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store

import com.rmldev.georgianalphabet.core.designsystem.font.FontCatalog
import com.rmldev.georgianalphabet.core.ui.ActorScope
import com.rmldev.georgianalphabet.core.ui.MviActor
import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.GetLetterUseCase
import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.ObserveLettersProgressUseCase
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.message.AllFontsAction
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.message.AllFontsEvent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.message.AllFontsSideEffect
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.state.AllFontsState
import com.rmldev.georgianalphabet.feature.preferences.api.usecase.ObserveUserPreferencesUseCase
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private typealias AllFontsActorScope = ActorScope<AllFontsState, AllFontsEvent, AllFontsSideEffect>

internal class AllFontsActor @Inject constructor(
    private val getLetter: GetLetterUseCase,
    private val observeLettersProgress: ObserveLettersProgressUseCase,
    private val observeUserPreferences: ObserveUserPreferencesUseCase,
) : MviActor<AllFontsAction, AllFontsState, AllFontsEvent, AllFontsSideEffect> {

    override suspend fun AllFontsActorScope.executeAction(action: AllFontsAction) {
        when (action) {
            AllFontsAction.LoadLetter -> event(AllFontsEvent.LetterLoaded(getLetter(state.letterId)))
            AllFontsAction.ObserveProgress -> observeProgress()
            is AllFontsAction.OpenFont -> openFont(action.fontId)
            AllFontsAction.Back -> sideEffect(AllFontsSideEffect.Back)
        }
    }

    // A locked font has no page on the card, so it can't be opened
    private suspend fun AllFontsActorScope.openFont(fontId: String) {
        if (FontCatalog.unlocked(state.unlockedFontIds).any { it.id == fontId }) {
            sideEffect(AllFontsSideEffect.OpenFont(state.letterId, fontId))
        }
    }

    private suspend fun AllFontsActorScope.observeProgress() {
        val id = state.letterId
        val progress = observeLettersProgress().map { it[id] }.distinctUntilChanged()
        val knownScript = observeUserPreferences().map { it.knownScript }.distinctUntilChanged()
        combine(progress, knownScript) { letterProgress, script ->
            AllFontsEvent.ProgressUpdated(letterProgress, script)
        }.collect { event(it) }
    }
}
