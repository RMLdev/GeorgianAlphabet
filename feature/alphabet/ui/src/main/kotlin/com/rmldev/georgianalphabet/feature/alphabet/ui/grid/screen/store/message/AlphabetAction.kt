package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message

import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetFilter
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetSort

internal sealed interface AlphabetAction {
    /** Подписка на буквы, прогресс и знакомый алфавит. Не завершается, пока жив экран. */
    object ObserveLetters : AlphabetAction

    data class OpenGlyph(val id: Int) : AlphabetAction

    data class SelectFilter(val filter: AlphabetFilter) : AlphabetAction

    data class SelectSort(val sort: AlphabetSort) : AlphabetAction

    object ToggleFontShuffle : AlphabetAction
}
