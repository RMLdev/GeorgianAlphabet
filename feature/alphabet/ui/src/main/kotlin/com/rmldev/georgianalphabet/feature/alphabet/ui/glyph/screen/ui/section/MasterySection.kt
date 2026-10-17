package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsIntent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.MasteryUi

/**
 * Блок «Освоение» (задача 26): уровень, «N из M шрифтов узнаёшь · точность X%», сетка ячеек шрифтов, легенда.
 * Заглушка, пока ничего не рисует. Строки — в `strings_glyph_mastery.xml`.
 */
@Suppress("UnusedParameter")
@Composable
internal fun MasterySection(state: MasteryUi, onIntent: (GlyphDetailsIntent) -> Unit, modifier: Modifier = Modifier) {
    // Stub until task 26
}
