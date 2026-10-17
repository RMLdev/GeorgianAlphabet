package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state

import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

internal class AlphabetStateToUiMapper @Inject constructor() : (AlphabetState) -> AlphabetUiState {

    override fun invoke(state: AlphabetState): AlphabetUiState {
        fun List<Letter>.toGlyphs() = map { it.toGlyphUi(state.progress[it.id], state.knownScript) }

        val (archaic, modern) = state.letters.partition { it.archaic }
        val glyphs = modern.toGlyphs()
        return AlphabetUiState(
            lettersCount = glyphs.size,
            masteredCount = glyphs.count { it.progress == GlyphLearningProgress.MASTERED },
            gridItems = glyphs.map(AlphabetGridItem::Glyph).toImmutableList(),
            archaicGlyphs = archaic.toGlyphs().toImmutableList(),
        )
    }
}
