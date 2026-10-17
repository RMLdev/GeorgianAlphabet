package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store

import com.rmldev.georgianalphabet.core.ui.MviReducer
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.message.AllFontsEvent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.state.AllFontsState
import javax.inject.Inject

internal class AllFontsReducer @Inject constructor() : MviReducer<AllFontsState, AllFontsEvent> {

    override fun reduce(oldState: AllFontsState, event: AllFontsEvent): AllFontsState = when (event) {
        is AllFontsEvent.LetterLoaded -> oldState.copy(isLoaded = true, letter = event.letter)

        is AllFontsEvent.ProgressUpdated -> oldState.copy(progress = event.progress, knownScript = event.knownScript)
    }
}
