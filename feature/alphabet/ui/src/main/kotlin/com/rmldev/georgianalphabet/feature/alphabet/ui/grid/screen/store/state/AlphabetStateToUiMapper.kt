package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state

import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

internal class AlphabetStateToUiMapper @Inject constructor() : (AlphabetState) -> AlphabetUiState {

    override fun invoke(state: AlphabetState): AlphabetUiState {
        fun List<Letter>.toGlyphs() = map { it.toGlyphUi(state.progress[it.id], state.knownScript) }

        val (archaic, modern) = state.letters.partition { it.archaic }
        val glyphs = modern.toGlyphs()
        val visible = glyphs.filter { state.filter.matches(it.progress) }
        return AlphabetUiState(
            lettersCount = glyphs.size,
            masteredCount = glyphs.countMastered(),
            filters = AlphabetFilter.entries
                .map { filter -> AlphabetFilterUi(filter, glyphs.count { filter.matches(it.progress) }) }
                .toImmutableList(),
            selectedFilter = state.filter,
            selectedSort = state.sort,
            isFilterResultEmpty = glyphs.isNotEmpty() && visible.isEmpty(),
            gridItems = when (state.sort) {
                AlphabetSort.ALPHABET -> visible.map(AlphabetGridItem::Glyph)
                AlphabetSort.LESSONS -> glyphs.toLessonSections(state.filter)
            }.toImmutableList(),
            archaicGlyphs = archaic.toGlyphs().toImmutableList(),
            fontsShuffled = state.fontShuffleSeed != null,
            tileFonts = tileFonts(state.letters, state.fontShuffleSeed),
        )
    }

    /**
     * Секции «Урок N»: счётчик «освоено x из y» по всем буквам урока, плитки — только
     * прошедшие фильтр; урок без таких плиток скрывается целиком.
     */
    private fun List<GlyphUi>.toLessonSections(activeFilter: AlphabetFilter): List<AlphabetGridItem> =
        mapNotNull { glyph -> glyph.lesson?.let { it to glyph } }
            .groupBy(keySelector = { it.first }, valueTransform = { it.second })
            .toSortedMap()
            .flatMap { (lesson, lessonGlyphs) ->
                val matching = lessonGlyphs.filter { activeFilter.matches(it.progress) }
                if (matching.isEmpty()) {
                    emptyList()
                } else {
                    val header = AlphabetGridItem.LessonHeader(lesson, lessonGlyphs.countMastered(), lessonGlyphs.size)
                    listOf(header) + matching.map(AlphabetGridItem::Glyph)
                }
            }

    private fun List<GlyphUi>.countMastered() = count { it.progress == GlyphLearningProgress.MASTERED }
}
