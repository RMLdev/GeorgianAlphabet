package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.message

internal sealed interface AllFontsAction {
    /** Буква грузится один раз. */
    object LoadLetter : AllFontsAction

    /** Подписка на прогресс буквы и знакомый алфавит. Не завершается, пока жив экран. */
    object ObserveProgress : AllFontsAction

    data class OpenFont(val fontId: String) : AllFontsAction

    object Back : AllFontsAction
}
