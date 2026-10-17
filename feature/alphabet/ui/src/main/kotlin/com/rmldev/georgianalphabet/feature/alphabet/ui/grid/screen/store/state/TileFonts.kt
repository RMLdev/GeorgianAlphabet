package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state

import com.rmldev.georgianalphabet.core.designsystem.font.FontCatalog
import com.rmldev.georgianalphabet.core.designsystem.font.GeorgianFont
import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableMap
import kotlin.random.Random

/**
 * Случайный открытый шрифт для каждой буквы: один и тот же для одного [seed], соседние буквы
 * в порядке алфавита не повторяют шрифт. Без [seed] — пустая карта, плитки в шрифте по умолчанию.
 */
internal fun tileFonts(
    letters: List<Letter>,
    seed: Long?,
    // Unlocking fonts as a reward isn't stored yet, so only the starter fonts are open
    fonts: List<GeorgianFont> = FontCatalog.unlocked(),
): ImmutableMap<Int, GeorgianFont> {
    if (seed == null || fonts.isEmpty()) return persistentMapOf()
    val random = Random(seed)
    var previous: GeorgianFont? = null
    return letters.associate { letter ->
        val candidates = if (fonts.size > 1) fonts.filter { it != previous } else fonts
        val font = candidates.random(random)
        previous = font
        letter.id to font
    }.toImmutableMap()
}
