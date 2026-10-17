package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state

import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject
import kotlin.random.Random

class AlphabetStateToUiMapper @Inject constructor() : (AlphabetState) -> AlphabetUiState {

    override fun invoke(state: AlphabetState): AlphabetUiState {
        return AlphabetUiState(
            screenTitle = "Алфавит",
            glyphs = listOf(
                'ა', 'ბ', 'გ', 'დ', 'ე', 'ვ', 'ზ', 'თ', 'ი', 'კ', 'ლ',
                'მ', 'ნ', 'ო', 'პ', 'ჟ', 'რ', 'ს', 'ტ', 'უ', 'ფ', 'ქ',
                'ღ', 'ყ', 'შ', 'ჩ', 'ც', 'ძ', 'წ', 'ჭ', 'ხ', 'ჯ', 'ჰ'
            ).mapIndexed { index, ch ->
                GlyphUi(
                    index,
                    ch,
                    "ani",
                    Random.nextInt(0, 4).let {
                        GlyphLearningProgress.entries[it]
                    },
                )
            }.toImmutableList(),
            gridColumnsCount = 4,
            gridRowsCount = 9,
        )
    }
}