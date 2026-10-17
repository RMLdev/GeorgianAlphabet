package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme
import com.rmldev.georgianalphabet.core.designsystem.theme.GlyphTextStyle
import com.rmldev.georgianalphabet.feature.alphabet.ui.R
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.GlyphLearningProgress
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.GlyphUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private val LegendBarWidth = 24.dp
private val LegendSegmentHeight = 4.dp
private val LegendSegmentGap = 2.dp
private const val LEGEND_SEGMENTS = 3

private val ArchaicTileHeight = 76.dp
private val ArchaicBorderWidth = 1.dp
private val ArchaicDash = 4.dp
private const val ARCHAIC_COLUMNS = 5

/** Legend under the grid: every mastery level with a sample of the tile's segment bar. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MasteryLegend(modifier: Modifier = Modifier) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(start = 4.dp, end = 4.dp, top = 4.dp),
    ) {
        GlyphLearningProgress.entries.forEach { progress ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                LegendBar(progress)
                Text(
                    text = stringResource(progress.legendLabelRes),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun LegendBar(progress: GlyphLearningProgress) {
    val colors = GeAlphTheme.learningColors
    val filled = progress.ordinal
    val filledColor = colors.mastery(filled)
    Row(
        horizontalArrangement = Arrangement.spacedBy(LegendSegmentGap),
        modifier = Modifier.width(LegendBarWidth),
    ) {
        repeat(LEGEND_SEGMENTS) { index ->
            Box(
                Modifier
                    .weight(1f)
                    .height(LegendSegmentHeight)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(if (index < filled) filledColor else colors.masteryEmpty),
            )
        }
    }
}

/**
 * Reference block of the archaic letters: a divider, a title with an explanation and a row of
 * tiles without a background and with a dashed outline. The tiles show only the name; tapping
 * one opens the letter card.
 */
@Composable
internal fun ArchaicLetters(
    glyphs: ImmutableList<GlyphUi>,
    onGlyphClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
        ) {
            Text(
                text = stringResource(R.string.feature_alphabet_ui_archaic_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.semantics { heading() },
            )
            Text(
                text = stringResource(R.string.feature_alphabet_ui_archaic_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        glyphs.chunked(ARCHAIC_COLUMNS).forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            ) {
                row.forEach { glyph ->
                    ArchaicLetterTile(
                        glyph = glyph,
                        onClick = { onGlyphClick(glyph.id) },
                        modifier = Modifier.weight(1f),
                    )
                }
                // keep the tiles of an incomplete last row as wide as the full ones
                repeat(ARCHAIC_COLUMNS - row.size) { Box(Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun ArchaicLetterTile(glyph: GlyphUi, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val shape = MaterialTheme.shapes.medium
    val outline = MaterialTheme.colorScheme.outline
    val description = stringResource(R.string.feature_alphabet_ui_archaic_tile_description, glyph.name)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
        modifier = modifier
            .height(ArchaicTileHeight)
            .clip(shape)
            .drawBehind {
                val stroke = ArchaicBorderWidth.toPx()
                val radius = (shape.topStart.toPx(size, this) - stroke / 2).coerceAtLeast(0f)
                drawRoundRect(
                    color = outline,
                    topLeft = Offset(stroke / 2, stroke / 2),
                    size = Size(size.width - stroke, size.height - stroke),
                    cornerRadius = CornerRadius(radius),
                    style = Stroke(
                        width = stroke,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(ArchaicDash.toPx(), ArchaicDash.toPx())),
                    ),
                )
            }
            .clickable(onClick = onClick)
            .clearAndSetSemantics {
                contentDescription = description
                role = Role.Button
            }
            .padding(horizontal = 2.dp),
    ) {
        Text(
            text = glyph.symbol.toString(),
            style = GlyphTextStyle.Tile,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = glyph.name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Clip,
        )
    }
}

@get:StringRes
private val GlyphLearningProgress.legendLabelRes: Int
    get() = when (this) {
        GlyphLearningProgress.NEW -> R.string.feature_alphabet_ui_legend_new
        GlyphLearningProgress.STARTED -> R.string.feature_alphabet_ui_legend_familiar
        GlyphLearningProgress.ADVANCED -> R.string.feature_alphabet_ui_legend_confident
        GlyphLearningProgress.MASTERED -> R.string.feature_alphabet_ui_legend_master
    }

private val PreviewArchaicGlyphs = persistentListOf(
    GlyphUi(33, 'ჱ', "хе", null, GlyphLearningProgress.NEW, null),
    GlyphUi(34, 'ჲ', "йе", null, GlyphLearningProgress.NEW, null),
    GlyphUi(35, 'ჳ', "ви", null, GlyphLearningProgress.NEW, null),
    GlyphUi(36, 'ჴ', "хар", null, GlyphLearningProgress.NEW, null),
    GlyphUi(37, 'ჵ', "хоэ", null, GlyphLearningProgress.NEW, null),
)

@Composable
private fun AlphabetReferencePreview(darkTheme: Boolean) {
    GeAlphTheme(darkTheme = darkTheme) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(Modifier.padding(16.dp)) {
                MasteryLegend()
                ArchaicLetters(PreviewArchaicGlyphs, onGlyphClick = {})
            }
        }
    }
}

@Preview(name = "light", widthDp = 360)
@Composable
private fun AlphabetReferenceLightPreview() = AlphabetReferencePreview(darkTheme = false)

@Preview(name = "dark", widthDp = 360)
@Composable
private fun AlphabetReferenceDarkPreview() = AlphabetReferencePreview(darkTheme = true)
