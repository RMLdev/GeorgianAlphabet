package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state

import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

internal class AlphabetStateToUiMapper @Inject constructor() : (AlphabetState) -> AlphabetUiState {

    override fun invoke(state: AlphabetState): AlphabetUiState {
        return AlphabetUiState(
            glyphs = state.letters.map { letter ->
                GlyphUi(
                    id = letter.id,
                    symbol = letter.symbol,
                    name = letter.name,
                    // Learning progress isn't stored yet
                    progress = GlyphLearningProgress.NEW,
                )
            }.toImmutableList(),
            gridColumnsCount = GRID_COLUMNS_COUNT,
            gridRowsCount = (state.letters.size + GRID_COLUMNS_COUNT - 1) / GRID_COLUMNS_COUNT,
        )
    }

    private companion object {
        const val GRID_COLUMNS_COUNT = 4
    }
}
