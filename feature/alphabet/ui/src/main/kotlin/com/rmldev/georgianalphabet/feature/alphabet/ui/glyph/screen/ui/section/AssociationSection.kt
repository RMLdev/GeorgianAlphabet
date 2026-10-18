package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme
import com.rmldev.georgianalphabet.core.designsystem.theme.GlyphTextStyle
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LocalizedText
import com.rmldev.georgianalphabet.feature.alphabet.ui.R
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsIntent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.localized

/**
 * Ассоциация: плейсхолдер иллюстрации с глифом [symbol] и фраза на языке интерфейса.
 * Пока иллюстраций нет, вместо картинки пунктирная рамка с буквой. При [association] == null блок скрыт.
 */
@Suppress("UnusedParameter") // onIntent: the section has no actions yet
@Composable
internal fun AssociationSection(
    symbol: String,
    association: LocalizedText?,
    onIntent: (GlyphDetailsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (association == null) return
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
    ) {
        IllustrationPlaceholder(symbol)
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = stringResource(R.string.feature_alphabet_ui_glyph_association_title),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = association.localized(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun IllustrationPlaceholder(symbol: String) {
    val description = stringResource(R.string.feature_alphabet_ui_glyph_association_illustration, symbol)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(IllustrationSize)
            .background(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.shapes.extraLarge)
            .dashedBorder(color = MaterialTheme.colorScheme.outline, cornerRadius = IllustrationCornerRadius)
            .semantics { contentDescription = description },
    ) {
        Text(
            text = symbol,
            style = GlyphTextStyle.Card,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

private fun Modifier.dashedBorder(color: Color, cornerRadius: Dp): Modifier = drawBehind {
    val stroke = DashedBorderWidth.toPx()
    val inset = stroke / 2
    drawRoundRect(
        color = color,
        topLeft = Offset(inset, inset),
        size = Size(size.width - stroke, size.height - stroke),
        cornerRadius = CornerRadius(cornerRadius.toPx() - inset),
        style = Stroke(
            width = stroke,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(DashLength.toPx(), DashGap.toPx())),
        ),
    )
}

private val IllustrationSize = 104.dp

// Как MaterialTheme.shapes.extraLarge: рамку рисуем сами, потому что она пунктирная
private val IllustrationCornerRadius = 28.dp
private val DashedBorderWidth = 1.5.dp
private val DashLength = 6.dp
private val DashGap = 4.dp

private val PreviewAssociation = LocalizedText(
    ru = "Круглый бочок и хвостик сверху, как у арбуза с веточкой.",
    en = "A round belly with a little tail on top, like a watermelon with a stem.",
)

@Preview(name = "light", widthDp = 360)
@Composable
private fun AssociationSectionLightPreview() {
    GeAlphTheme(darkTheme = false) {
        Box(Modifier.background(MaterialTheme.colorScheme.background)) {
            AssociationSection("ა", PreviewAssociation, onIntent = {})
        }
    }
}

@Preview(name = "dark", widthDp = 360)
@Composable
private fun AssociationSectionDarkPreview() {
    GeAlphTheme(darkTheme = true) {
        Box(Modifier.background(MaterialTheme.colorScheme.background)) {
            AssociationSection("ა", PreviewAssociation, onIntent = {})
        }
    }
}
