package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme
import com.rmldev.georgianalphabet.core.designsystem.theme.GlyphTextStyle
import com.rmldev.georgianalphabet.feature.alphabet.ui.R
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsIntent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.SimilarLetterUi
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.SimilarLettersUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private val ChipHeight = 56.dp
private val ChipTileSize = 40.dp

/**
 * «Похожие буквы»: строки «по виду» и «по звуку», чип открывает карточку буквы.
 * Пустая строка скрывается, обе пустые — блок целиком. Строки — в `strings_glyph_similar.xml`.
 */
@Composable
internal fun SimilarLetters(
    state: SimilarLettersUi,
    onIntent: (GlyphDetailsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.byShape.isEmpty() && state.bySound.isEmpty()) return
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 28.dp, end = 16.dp),
    ) {
        Text(
            text = stringResource(R.string.feature_alphabet_ui_glyph_similar_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .semantics { heading() },
        )
        SimilarRow(R.string.feature_alphabet_ui_glyph_similar_by_shape, state.byShape, onIntent)
        SimilarRow(R.string.feature_alphabet_ui_glyph_similar_by_sound, state.bySound, onIntent)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SimilarRow(
    @StringRes labelRes: Int,
    letters: ImmutableList<SimilarLetterUi>,
    onIntent: (GlyphDetailsIntent) -> Unit,
) {
    if (letters.isEmpty()) return
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 4.dp),
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            letters.forEach { letter ->
                SimilarLetterChip(letter, onClick = { onIntent(GlyphDetailsIntent.SimilarClicked(letter.id)) })
            }
        }
    }
}

@Composable
private fun SimilarLetterChip(letter: SimilarLetterUi, onClick: () -> Unit) {
    val description = stringResource(R.string.feature_alphabet_ui_glyph_similar_letter_description, letter.name)
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .height(ChipHeight)
                .clearAndSetSemantics { contentDescription = description }
                .padding(start = 8.dp, end = 18.dp),
        ) {
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier.size(ChipTileSize),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = letter.symbol, style = GlyphTextStyle.Tile)
                }
            }
            Text(text = letter.name, style = MaterialTheme.typography.labelLarge)
        }
    }
}

private val PreviewSimilar = SimilarLettersUi(
    byShape = persistentListOf(SimilarLetterUi(17, "ს", "сани"), SimilarLetterUi(32, "ჰ", "хаэ")),
    bySound = persistentListOf(SimilarLetterUi(20, "ფ", "пхари")),
)

@Preview(name = "light", widthDp = 360)
@Composable
private fun SimilarLettersLightPreview() {
    GeAlphTheme(darkTheme = false) {
        Surface { SimilarLetters(PreviewSimilar, onIntent = {}) }
    }
}

@Preview(name = "dark", widthDp = 360)
@Composable
private fun SimilarLettersDarkPreview() {
    GeAlphTheme(darkTheme = true) {
        Surface { SimilarLetters(PreviewSimilar, onIntent = {}) }
    }
}
