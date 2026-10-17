package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state

/** Фильтр сетки по освоению: «Все / Новые / Изучаю / Освоены». */
enum class AlphabetFilter {
    ALL,
    NEW,

    /** Знакомые и уверенные буквы. */
    LEARNING,
    MASTERED,
}

/** Порядок сетки: сплошная сетка по алфавиту или секции по урокам. */
enum class AlphabetSort {
    ALPHABET,
    LESSONS,
}

internal fun AlphabetFilter.matches(progress: GlyphLearningProgress): Boolean = when (this) {
    AlphabetFilter.ALL -> true
    AlphabetFilter.NEW -> progress == GlyphLearningProgress.NEW
    AlphabetFilter.LEARNING -> progress == GlyphLearningProgress.STARTED || progress == GlyphLearningProgress.ADVANCED
    AlphabetFilter.MASTERED -> progress == GlyphLearningProgress.MASTERED
}
