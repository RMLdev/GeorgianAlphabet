package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message

import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LetterDetails
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LetterProgress
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript

internal sealed interface GlyphDetailsEvent {
    /** [letter] и [details] — null, если буквы с таким id нет. [relatedLetters] — буквы из `similarBy*`. */
    data class LetterLoaded(val letter: Letter?, val details: LetterDetails?, val relatedLetters: List<Letter>) :
        GlyphDetailsEvent

    /** [progress] — null, пока по букве не было ответов. */
    data class ProgressUpdated(val progress: LetterProgress?, val knownScript: KnownScript) : GlyphDetailsEvent

    data class FontSelected(val fontId: String) : GlyphDetailsEvent

    /** Путь звука, который сейчас играет, или null. */
    data class PlayingChanged(val playingId: String?) : GlyphDetailsEvent
}
