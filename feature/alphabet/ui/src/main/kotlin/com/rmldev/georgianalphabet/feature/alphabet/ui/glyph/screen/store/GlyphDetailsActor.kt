package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store

import com.rmldev.georgianalphabet.core.audio.api.AudioAssets
import com.rmldev.georgianalphabet.core.audio.api.SoundPlayer
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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

private typealias GlyphDetailsActorScope = ActorScope<GlyphDetailsState, GlyphDetailsEvent, GlyphDetailsSideEffect>

internal class GlyphDetailsActor @Inject constructor(
    private val getLetters: GetLettersUseCase,
    private val getLetterDetails: GetLetterDetailsUseCase,
    private val observeLettersProgress: ObserveLettersProgressUseCase,
    private val observeUserPreferences: ObserveUserPreferencesUseCase,
    private val soundPlayer: SoundPlayer,
) : MviActor<GlyphDetailsAction, GlyphDetailsState, GlyphDetailsEvent, GlyphDetailsSideEffect> {

    override suspend fun GlyphDetailsActorScope.executeAction(action: GlyphDetailsAction) {
        when (action) {
            GlyphDetailsAction.LoadLetter -> loadLetter()

            GlyphDetailsAction.Observe -> coroutineScope {
                launch { observeProgress() }
                launch { observePlaying() }
            }

            is GlyphDetailsAction.SelectFont -> event(GlyphDetailsEvent.FontSelected(action.fontId))

            GlyphDetailsAction.PlayName -> play(AudioAssets.letter(state.glyphId))

            is GlyphDetailsAction.PlayWord ->
                play(state.details?.words?.firstOrNull { it.georgian == action.georgian }?.audioAsset)

            is GlyphDetailsAction.OpenLetter -> {
                stopSound()
                sideEffect(GlyphDetailsSideEffect.OpenLetter(action.letterId))
            }

            GlyphDetailsAction.OpenAllFonts -> {
                stopSound()
                sideEffect(GlyphDetailsSideEffect.OpenAllFonts(state.glyphId))
            }

            GlyphDetailsAction.Back -> {
                stopSound()
                sideEffect(GlyphDetailsSideEffect.Back)
            }
        }
    }

    /** Остановить звук: экран уходит со стека или с переднего плана. */
    fun stopSound() = soundPlayer.stop()

    // A missing file or a playback error stays silent: the button just does nothing (task 21 adds the files)
    private suspend fun play(assetPath: String?) {
        if (assetPath != null) soundPlayer.play(assetPath)
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

    private suspend fun GlyphDetailsActorScope.observePlaying() {
        soundPlayer.playing.collect { event(GlyphDetailsEvent.PlayingChanged(it)) }
    }
}
