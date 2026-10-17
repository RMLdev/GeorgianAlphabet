package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
data class AlphabetUiState(
    /** Буквы мхедрули без архаичных: они не входят ни в уроки, ни в счётчики. */
    val lettersCount: Int = 0,
    val masteredCount: Int = 0,
    /** Чипы фильтров по порядку со счётчиками букв. */
    val filters: ImmutableList<AlphabetFilterUi> = persistentListOf(),
    val selectedFilter: AlphabetFilter = AlphabetFilter.ALL,
    val selectedSort: AlphabetSort = AlphabetSort.ALPHABET,
    /** Под фильтр не попала ни одна буква: вместо пустой сетки показать сообщение. */
    val isFilterResultEmpty: Boolean = false,
    /** Содержимое сетки по порядку: плитки и заголовки секций на всю ширину. */
    val gridItems: ImmutableList<AlphabetGridItem> = persistentListOf(),
    /** Архаичные буквы для справочного блока под сеткой. */
    val archaicGlyphs: ImmutableList<GlyphUi> = persistentListOf(),
)

@Stable
data class AlphabetFilterUi(val filter: AlphabetFilter, val count: Int)

@Stable
sealed interface AlphabetGridItem {
    val key: String

    data class Glyph(val glyph: GlyphUi) : AlphabetGridItem {
        override val key: String get() = "glyph-${glyph.id}"
    }

    /** Заголовок секции урока в режиме «По урокам»: «Урок N · освоено x из y». */
    data class LessonHeader(val lesson: Int, val masteredCount: Int, val lettersCount: Int) : AlphabetGridItem {
        override val key: String get() = "lesson-$lesson"
    }
}

@Stable
data class GlyphUi(
    val id: Int,
    val symbol: Char,
    val name: String,
    // null for archaic letters: their tiles show only the name
    val sound: String?,
    val progress: GlyphLearningProgress,
    val lesson: Int?,
)

enum class GlyphLearningProgress {
    NEW,
    STARTED,
    ADVANCED,
    MASTERED,
}
