package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsIntent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.SimilarLettersUi

/**
 * «Похожие буквы» (задача 28): строки «по виду» и «по звуку», чип открывает карточку буквы.
 * Заглушка: все чипы одной строкой, чтобы проверить переход. Строки — в `strings_glyph_similar.xml`.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun SimilarLetters(
    state: SimilarLettersUi,
    onIntent: (GlyphDetailsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
    ) {
        (state.byShape + state.bySound).distinctBy { it.id }.forEach { letter ->
            SuggestionChip(
                onClick = { onIntent(GlyphDetailsIntent.SimilarClicked(letter.id)) },
                label = { Text("${letter.symbol} ${letter.name}") },
            )
        }
    }
}
