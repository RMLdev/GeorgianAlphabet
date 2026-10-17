package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.feature.alphabet.ui.R
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsIntent

/**
 * Низ карточки (задача 28): строка «Артикуляция · скоро» и кнопка «Все шрифты».
 * Заглушка: только кнопка. Строки — в `strings_glyph_similar.xml`.
 */
@Composable
internal fun CardActions(onIntent: (GlyphDetailsIntent) -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = { onIntent(GlyphDetailsIntent.AllFontsClicked) },
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
    ) {
        Text(stringResource(R.string.feature_alphabet_ui_glyph_all_fonts))
    }
}
