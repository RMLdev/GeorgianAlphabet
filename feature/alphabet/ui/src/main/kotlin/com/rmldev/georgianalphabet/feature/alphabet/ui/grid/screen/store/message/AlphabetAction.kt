package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message

internal sealed interface AlphabetAction {
    /** Подписка на буквы, прогресс и знакомый алфавит. Не завершается, пока жив экран. */
    object ObserveLetters : AlphabetAction

    data class OpenGlyph(val id: Int) : AlphabetAction
}
