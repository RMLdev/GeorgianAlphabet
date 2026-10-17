package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state

import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LetterProgress
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript

internal data class AlphabetState(
    /** Все буквы, включая архаичные, по [Letter.order]. */
    val letters: List<Letter> = emptyList(),
    /** Прогресс по id буквы; буквы без записи — новые. */
    val progress: Map<Int, LetterProgress> = emptyMap(),
    val knownScript: KnownScript = KnownScript.CYRILLIC,
    /** Выбранный фильтр живёт только пока жив экран, между запусками не сохраняется. */
    val filter: AlphabetFilter = AlphabetFilter.ALL,
    val sort: AlphabetSort = AlphabetSort.ALPHABET,
)
