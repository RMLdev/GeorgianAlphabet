package com.rmldev.georgianalphabet.feature.alphabet.ui.letters

/**
 * The 33 letters of the modern Mkhedruli alphabet with romanized names.
 *
 * Temporary data source for the feature screens, to be replaced by an alphabet
 * repository in `feature:alphabet:impl`.
 */
internal data class GeorgianLetter(val id: Int, val symbol: Char, val name: String)

internal val MKHEDRULI: List<GeorgianLetter> = listOf(
    'ა' to "ani", 'ბ' to "bani", 'გ' to "gani", 'დ' to "doni", 'ე' to "eni",
    'ვ' to "vini", 'ზ' to "zeni", 'თ' to "tani", 'ი' to "ini", 'კ' to "k'ani",
    'ლ' to "lasi", 'მ' to "mani", 'ნ' to "nari", 'ო' to "oni", 'პ' to "p'ari",
    'ჟ' to "zhani", 'რ' to "rae", 'ს' to "sani", 'ტ' to "t'ari", 'უ' to "uni",
    'ფ' to "pari", 'ქ' to "kani", 'ღ' to "ghani", 'ყ' to "q'ari", 'შ' to "shini",
    'ჩ' to "chini", 'ც' to "tsani", 'ძ' to "dzili", 'წ' to "ts'ili", 'ჭ' to "ch'ari",
    'ხ' to "khani", 'ჯ' to "jani", 'ჰ' to "hae",
).mapIndexed { index, (symbol, name) -> GeorgianLetter(index, symbol, name) }

internal fun mkhedruliLetter(id: Int): GeorgianLetter =
    requireNotNull(MKHEDRULI.getOrNull(id)) { "Unknown letter id: $id" }
