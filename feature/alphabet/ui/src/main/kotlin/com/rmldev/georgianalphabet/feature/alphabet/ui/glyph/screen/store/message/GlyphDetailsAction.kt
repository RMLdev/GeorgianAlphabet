package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message

internal sealed interface GlyphDetailsAction {
    /** Буква, её детали и похожие буквы: грузятся один раз. */
    object LoadLetter : GlyphDetailsAction

    /** Подписки на прогресс буквы, знакомый алфавит и играющий звук. Не завершается, пока жив экран. */
    object Observe : GlyphDetailsAction

    data class SelectFont(val fontId: String) : GlyphDetailsAction

    /** Название буквы: `audio/letters/<id>.mp3`. */
    object PlayName : GlyphDetailsAction

    /** Слово-пример по его мхедрули; играет `audioAsset` слова, без файла — ничего. */
    data class PlayWord(val georgian: String) : GlyphDetailsAction

    data class OpenLetter(val letterId: Int) : GlyphDetailsAction

    object OpenAllFonts : GlyphDetailsAction

    object Back : GlyphDetailsAction
}
