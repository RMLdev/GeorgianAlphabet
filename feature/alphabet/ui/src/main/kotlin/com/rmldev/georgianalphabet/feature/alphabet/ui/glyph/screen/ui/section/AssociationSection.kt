package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LocalizedText
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsIntent

/**
 * Ассоциация (задача 27): плейсхолдер иллюстрации с глифом [symbol] и фраза на языке интерфейса
 * (`LocalizedText.localized()`); при [association] == null блок скрыт.
 * Заглушка, пока ничего не рисует. Строки — в `strings_glyph_words.xml`.
 */
@Suppress("UnusedParameter")
@Composable
internal fun AssociationSection(
    symbol: String,
    association: LocalizedText?,
    onIntent: (GlyphDetailsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Stub until task 27
}
