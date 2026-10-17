package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store

import com.rmldev.georgianalphabet.core.ui.MviReducer
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsEvent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.GlyphDetailsState
import javax.inject.Inject

internal class GlyphDetailsReducer @Inject constructor() : MviReducer<GlyphDetailsState, GlyphDetailsEvent> {

    override fun reduce(oldState: GlyphDetailsState, event: GlyphDetailsEvent): GlyphDetailsState = when (event) {
        is GlyphDetailsEvent.LetterLoaded -> oldState.copy(
            isLoaded = true,
            letter = event.letter,
            details = event.details,
            relatedLetters = event.relatedLetters.associateBy { it.id },
        )

        is GlyphDetailsEvent.ProgressUpdated -> oldState.copy(
            progress = event.progress,
            knownScript = event.knownScript,
        )

        is GlyphDetailsEvent.FontSelected -> oldState.copy(currentFontId = event.fontId)

        is GlyphDetailsEvent.PlayingChanged -> oldState.copy(playingId = event.playingId)
    }
}
