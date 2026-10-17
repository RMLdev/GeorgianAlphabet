package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsIntent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.LetterHeaderUi

/**
 * Название, транслит, МФА и кнопка звука (задача 25).
 * Заглушка: название по-грузински и на знакомом алфавите. Строки — в `strings_glyph_header.xml`.
 */
@Suppress("UnusedParameter") // onIntent: PlayName comes with the sound button
@Composable
internal fun LetterHeader(
    state: LetterHeaderUi,
    onIntent: (GlyphDetailsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
    ) {
        Text(state.nameGeorgian, style = MaterialTheme.typography.headlineLarge)
        Text(
            state.name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
