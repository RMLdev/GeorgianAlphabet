package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message

internal sealed interface GlyphDetailsIntent {
    /** Сцена перелистнута на открытый шрифт; страница «Ещё N шрифтов» шрифтом не считается. */
    data class FontSelected(val fontId: String) : GlyphDetailsIntent

    /** Кнопка звука у названия буквы. */
    object PlayName : GlyphDetailsIntent

    /** «Послушать слово» у примера; [georgian] — слово мхедрули из деталей буквы. */
    data class PlayWord(val georgian: String) : GlyphDetailsIntent

    /** Чип похожей буквы. */
    data class SimilarClicked(val letterId: Int) : GlyphDetailsIntent

    /** Иконка в тулбаре, чип шрифта, страница закрытых шрифтов и кнопка внизу карточки. */
    object AllFontsClicked : GlyphDetailsIntent

    object BackClicked : GlyphDetailsIntent
}
