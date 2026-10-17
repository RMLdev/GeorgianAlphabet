package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rmldev.georgianalphabet.core.designsystem.icon.GeAlphIcons
import com.rmldev.georgianalphabet.feature.alphabet.ui.R
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsIntent

/**
 * Тулбар поверх сцены (задача 24): «Назад к алфавиту», заголовок «Буква ა» при схлопывании, «Все шрифты».
 * Заглушка: только две кнопки. Строки — в `strings_glyph_toolbar.xml`.
 */
@Suppress("UnusedParameter") // symbol: the "Буква ა" title appears when the stage collapses
@Composable
internal fun GlyphTopBar(
    symbol: String,
    onIntent: (GlyphDetailsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        IconButton(onClick = { onIntent(GlyphDetailsIntent.BackClicked) }) {
            Icon(GeAlphIcons.ArrowBack, contentDescription = stringResource(R.string.feature_alphabet_ui_glyph_back))
        }
        Spacer(Modifier.weight(1f))
        IconButton(onClick = { onIntent(GlyphDetailsIntent.AllFontsClicked) }) {
            Icon(
                GeAlphIcons.AllFonts,
                contentDescription = stringResource(R.string.feature_alphabet_ui_glyph_toolbar_all_fonts),
            )
        }
    }
}
