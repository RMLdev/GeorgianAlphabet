package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsIntent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.ExampleWordUi
import kotlinx.collections.immutable.ImmutableList

/**
 * «Примеры слов» (задача 27): слово с подсветкой буквы, «транслит · перевод», кнопка «Послушать слово»
 * (`PlayWord`); пустой список — блок скрыт.
 * Заглушка, пока ничего не рисует. Строки — в `strings_glyph_words.xml`.
 */
@Suppress("UnusedParameter")
@Composable
internal fun ExampleWords(
    words: ImmutableList<ExampleWordUi>,
    onIntent: (GlyphDetailsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Stub until task 27
}
