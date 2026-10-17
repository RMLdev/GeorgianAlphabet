package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message

internal sealed interface GlyphDetailsAction {
    /** Буква, её детали и похожие буквы: грузятся один раз. */
    object LoadLetter : GlyphDetailsAction

    /** Подписка на прогресс буквы и знакомый алфавит. Не завершается, пока жив экран. */
    object ObserveProgress : GlyphDetailsAction

    data class SelectFont(val fontId: String) : GlyphDetailsAction

    object PlayName : GlyphDetailsAction

    data class PlayWord(val georgian: String) : GlyphDetailsAction

    data class OpenLetter(val letterId: Int) : GlyphDetailsAction

    object OpenAllFonts : GlyphDetailsAction

    object Back : GlyphDetailsAction
}
