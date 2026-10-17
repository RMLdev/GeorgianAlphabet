package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message

import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetFilter
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetSort

internal sealed interface AlphabetIntent {

    data class GlyphClicked(val id: Int) : AlphabetIntent

    data class FilterSelected(val filter: AlphabetFilter) : AlphabetIntent

    data class SortSelected(val sort: AlphabetSort) : AlphabetIntent

    object ShuffleFontsClicked : AlphabetIntent
}
