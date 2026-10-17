package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.message

internal sealed interface AllFontsSideEffect {
    /** Вернуться на карточку буквы [letterId] и показать её в шрифте [fontId]. */
    data class OpenFont(val letterId: Int, val fontId: String) : AllFontsSideEffect

    object Back : AllFontsSideEffect
}
