package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.message

import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LetterProgress
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript

internal sealed interface AllFontsEvent {
    /** [letter] — null, если буквы с таким id нет. */
    data class LetterLoaded(val letter: Letter?) : AllFontsEvent

    /** [progress] — null, пока по букве не было ответов. */
    data class ProgressUpdated(val progress: LetterProgress?, val knownScript: KnownScript) : AllFontsEvent
}
