package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.message

internal sealed interface AllFontsIntent {
    /** Плитка открытого шрифта: вернуться на карточку буквы с этим шрифтом. */
    data class FontClicked(val fontId: String) : AllFontsIntent

    object BackClicked : AllFontsIntent
}
