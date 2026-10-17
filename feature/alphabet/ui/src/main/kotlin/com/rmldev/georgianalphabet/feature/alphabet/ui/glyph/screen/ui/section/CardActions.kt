package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.icon.GeAlphIcons
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme
import com.rmldev.georgianalphabet.feature.alphabet.ui.R
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsIntent

private val RowMinHeight = 56.dp
private val SoonBadgeHeight = 24.dp
private val ButtonHeight = 56.dp

/**
 * Низ карточки: неактивная строка «Артикуляция · скоро» (схемы отложены по концепции)
 * и кнопка «Все шрифты». «Потренировать эту букву» появится вместе с режимами заданий.
 * Строки — в `strings_glyph_similar.xml`.
 */
@Composable
internal fun CardActions(onIntent: (GlyphDetailsIntent) -> Unit, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(28.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 28.dp, end = 16.dp, bottom = 32.dp),
    ) {
        ArticulationRow()
        OutlinedButton(
            onClick = { onIntent(GlyphDetailsIntent.AllFontsClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .height(ButtonHeight),
        ) {
            Text(stringResource(R.string.feature_alphabet_ui_glyph_all_fonts))
        }
    }
}

@Composable
private fun ArticulationRow() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = RowMinHeight)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .semantics(mergeDescendants = true) { disabled() }
            .padding(horizontal = 16.dp),
    ) {
        val contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        Icon(imageVector = GeAlphIcons.Articulation, contentDescription = null, tint = contentColor)
        Text(
            text = stringResource(R.string.feature_alphabet_ui_glyph_articulation),
            style = MaterialTheme.typography.bodyLarge,
            color = contentColor,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = stringResource(R.string.feature_alphabet_ui_glyph_articulation_soon),
            style = MaterialTheme.typography.labelMedium,
            color = contentColor,
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                .heightIn(min = SoonBadgeHeight)
                .padding(horizontal = 10.dp, vertical = 4.dp),
        )
    }
}

@Preview(name = "light", widthDp = 360)
@Composable
private fun CardActionsLightPreview() {
    GeAlphTheme(darkTheme = false) {
        Surface { CardActions(onIntent = {}) }
    }
}

@Preview(name = "dark", widthDp = 360)
@Composable
private fun CardActionsDarkPreview() {
    GeAlphTheme(darkTheme = true) {
        Surface { CardActions(onIntent = {}) }
    }
}
