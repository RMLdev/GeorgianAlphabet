package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state

import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LetterDetails
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LetterProgress
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript

internal data class GlyphDetailsState(
    val glyphId: Int,
    /** Шрифт сцены; null или закрытый шрифт — первый открытый. Переживает пересоздание экрана. */
    val currentFontId: String? = null,
    /** Загрузка завершена: если [letter] всё ещё null, буквы с таким id нет. */
    val isLoaded: Boolean = false,
    val letter: Letter? = null,
    val details: LetterDetails? = null,
    /** Буквы из `similarBy*` деталей по id, для чипов «Похожие буквы». */
    val relatedLetters: Map<Int, Letter> = emptyMap(),
    /** null — по букве ещё не было ответов, она новая. */
    val progress: LetterProgress? = null,
    val knownScript: KnownScript = KnownScript.CYRILLIC,
    /** Шрифты, открытые наградой, сверх стартовых. Открытие пока не хранится, поэтому пусто. */
    val unlockedFontIds: Set<String> = emptySet(),
    /** Путь звука, который сейчас играет (`SoundPlayer.playing`); null — тишина. */
    val playingId: String? = null,
)
