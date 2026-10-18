package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.state

import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LetterProgress
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript

internal data class AllFontsState(
    val letterId: Int,
    /** Загрузка завершена: если [letter] всё ещё null, буквы с таким id нет. */
    val isLoaded: Boolean = false,
    val letter: Letter? = null,
    /** null — по букве ещё не было ответов. */
    val progress: LetterProgress? = null,
    val knownScript: KnownScript = KnownScript.CYRILLIC,
    /** Шрифты, открытые наградой, сверх стартовых. Открытие пока не хранится, поэтому пусто. */
    val unlockedFontIds: Set<String> = emptySet(),
)
