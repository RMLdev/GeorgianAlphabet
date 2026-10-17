package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state

import com.rmldev.georgianalphabet.feature.alphabet.ui.letters.GeorgianLetter
import com.rmldev.georgianalphabet.feature.alphabet.ui.letters.MKHEDRULI

internal data class AlphabetState(
    val letters: List<GeorgianLetter> = MKHEDRULI,
)
