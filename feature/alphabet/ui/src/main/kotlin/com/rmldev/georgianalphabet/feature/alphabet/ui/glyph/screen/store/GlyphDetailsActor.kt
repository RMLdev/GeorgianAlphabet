package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store

import com.rmldev.georgianalphabet.core.ui.ActorScope
import com.rmldev.georgianalphabet.core.ui.MviActor
import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.GetLetterDetailsUseCase
import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.GetLettersUseCase
import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.ObserveLettersProgressUseCase
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsAction
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsEvent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsSideEffect
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.GlyphDetailsState
import com.rmldev.georgianalphabet.feature.preferences.api.usecase.ObserveUserPreferencesUseCase
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private typealias GlyphDetailsActorScope = ActorScope<GlyphDetailsState, GlyphDetailsEvent, GlyphDetailsSideEffect>

internal class GlyphDetailsActor @Inject constructor(
    private val getLetters: GetLettersUseCase,
    private val getLetterDetails: GetLetterDetailsUseCase,
    private val observeLettersProgress: ObserveLettersProgressUseCase,
    private val observeUserPreferences: ObserveUserPreferencesUseCase,
) : MviActor<GlyphDetailsAction, GlyphDetailsState, GlyphDetailsEvent, GlyphDetailsSideEffect> {

    override suspend fun GlyphDetailsActorScope.executeAction(action: GlyphDetailsAction) {
        when (action) {
            GlyphDetailsAction.LoadLetter -> loadLetter()

            GlyphDetailsAction.ObserveProgress -> observeProgress()

            is GlyphDetailsAction.SelectFont -> event(GlyphDetailsEvent.FontSelected(action.fontId))

            // Sound is wired to SoundPlayer separately (task 29)
            GlyphDetailsAction.PlayName, is GlyphDetailsAction.PlayWord -> Unit

            is GlyphDetailsAction.OpenLetter -> sideEffect(GlyphDetailsSideEffect.OpenLetter(action.letterId))

            GlyphDetailsAction.OpenAllFonts -> sideEffect(GlyphDetailsSideEffect.OpenAllFonts(state.glyphId))

            GlyphDetailsAction.Back -> sideEffect(GlyphDetailsSideEffect.Back)
        }
    }

    private suspend fun GlyphDetailsActorScope.loadLetter() {
        val letters = getLetters().associateBy { it.id }
        val letter = letters[state.glyphId]
        val details = letter?.let { getLetterDetails(it.id) }
        val related = details
            ?.let { (it.similarByShape + it.similarBySound).distinct().mapNotNull(letters::get) }
            .orEmpty()
        event(GlyphDetailsEvent.LetterLoaded(letter, details, related))
    }

    private suspend fun GlyphDetailsActorScope.observeProgress() {
        val id = state.glyphId
        val progress = observeLettersProgress().map { it[id] }.distinctUntilChanged()
        val knownScript = observeUserPreferences().map { it.knownScript }.distinctUntilChanged()
        combine(progress, knownScript) { letterProgress, script ->
            GlyphDetailsEvent.ProgressUpdated(letterProgress, script)
        }.collect { event(it) }
    }
}
