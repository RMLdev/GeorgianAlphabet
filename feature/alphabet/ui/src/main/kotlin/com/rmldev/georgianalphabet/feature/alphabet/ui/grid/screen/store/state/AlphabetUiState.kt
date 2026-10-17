package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Stable
data class AlphabetUiState(
    val glyphs: ImmutableList<GlyphUi> = listOf<GlyphUi>().toImmutableList(),
    val gridColumnsCount: Int,
    val gridRowsCount: Int,
)

@Stable
data class GlyphUi(
    val id: Int,
    val symbol: Char,
    val name: String,
    // null for archaic letters: their tiles show only the name
    val sound: String?,
    val progress: GlyphLearningProgress,
)

enum class GlyphLearningProgress {
    NEW,
    STARTED,
    ADVANCED,
    MASTERED,
}
