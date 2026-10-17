package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.font.FontCatalog
import com.rmldev.georgianalphabet.core.designsystem.icon.GeAlphIcons
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme
import com.rmldev.georgianalphabet.core.designsystem.theme.GlyphTextStyle
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LearningLevel
import com.rmldev.georgianalphabet.feature.alphabet.ui.R
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsIntent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.FontCellStatus
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.FontCellUi
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.MasteryUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private const val GRID_COLUMNS = 8
private const val MASTERY_SEGMENTS = 3
private val CellGap = 6.dp
private val CellBorderWidth = 1.5.dp
private val CellLockSize = 14.dp
private val LevelBarWidth = 42.dp
private val LevelSegmentHeight = 4.dp
private val LevelSegmentGap = 3.dp
private val LegendSwatchSize = 10.dp
private val LegendSwatchShape = RoundedCornerShape(3.dp)

/**
 * Блок «Освоение» (задача 26): уровень буквы, «N из M шрифтов узнаёшь · точность X%»,
 * сетка ячеек всех шрифтов каталога и легенда статусов ячеек.
 */
@Suppress("UnusedParameter") // onIntent: the block has no actions yet, the slot keeps the section signature
@Composable
internal fun MasterySection(state: MasteryUi, onIntent: (GlyphDetailsIntent) -> Unit, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.extraLarge)
            .padding(20.dp),
    ) {
        LevelRow(state.level)
        RecognizedSummary(state)
        FontCellsGrid(state.symbol, state.cells)
        CellsLegend()
    }
}

@Composable
private fun LevelRow(level: LearningLevel) {
    val title = stringResource(R.string.feature_alphabet_ui_mastery_title)
    val levelLabel = stringResource(level.labelRes)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clearAndSetSemantics {
                contentDescription = "$title: $levelLabel"
            },
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
        LevelBar(level)
        Text(
            text = levelLabel,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = level.labelColor(),
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@Composable
private fun LevelBar(level: LearningLevel) {
    val colors = GeAlphTheme.learningColors
    val filled = level.ordinal
    val filledColor = colors.mastery(filled)
    Row(
        horizontalArrangement = Arrangement.spacedBy(LevelSegmentGap),
        modifier = Modifier.width(LevelBarWidth),
    ) {
        repeat(MASTERY_SEGMENTS) { index ->
            Box(
                Modifier
                    .weight(1f)
                    .height(LevelSegmentHeight)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(if (index < filled) filledColor else colors.masteryEmpty),
            )
        }
    }
}

@Composable
private fun RecognizedSummary(state: MasteryUi) {
    val fonts = pluralStringResource(R.plurals.feature_alphabet_ui_mastery_fonts_recognized, state.fontsCount)
    val caption = state.accuracyPercent?.let {
        stringResource(R.string.feature_alphabet_ui_mastery_with_accuracy, fonts, it)
    } ?: fonts
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.semantics(mergeDescendants = true) {},
    ) {
        Text(
            text = stringResource(R.string.feature_alphabet_ui_mastery_count, state.recognizedCount, state.fontsCount),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.alignByBaseline(),
        )
        Text(
            text = caption,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.alignByBaseline(),
        )
    }
}

@Composable
private fun FontCellsGrid(symbol: String, cells: ImmutableList<FontCellUi>) {
    // Not a lazy grid: the block sits inside the card's LazyColumn and has at most two rows
    Column(verticalArrangement = Arrangement.spacedBy(CellGap)) {
        cells.chunked(GRID_COLUMNS).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(CellGap)) {
                row.forEach { cell ->
                    FontCell(symbol, cell, Modifier.weight(1f))
                }
                repeat(GRID_COLUMNS - row.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun FontCell(symbol: String, cell: FontCellUi, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val learning = GeAlphTheme.learningColors
    val shape = MaterialTheme.shapes.small
    val description = stringResource(
        R.string.feature_alphabet_ui_mastery_cell_description,
        cell.font.displayName,
        stringResource(cell.status.labelRes),
    )
    val background: Color
    val border: Color?
    when (cell.status) {
        FontCellStatus.RECOGNIZED -> {
            background = learning.masteryConfidentContainer
            border = learning.masteryConfident
        }

        FontCellStatus.LEARNING -> {
            background = Color.Transparent
            border = colors.outline
        }

        FontCellStatus.LOCKED -> {
            background = colors.surfaceContainerHighest
            border = null
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .aspectRatio(1f)
            .clip(shape)
            .background(background)
            .then(if (border != null) Modifier.border(CellBorderWidth, border, shape) else Modifier)
            .clearAndSetSemantics { contentDescription = description },
    ) {
        if (cell.status == FontCellStatus.LOCKED) {
            Icon(
                imageVector = GeAlphIcons.Lock,
                contentDescription = null,
                tint = colors.onSurfaceVariant,
                modifier = Modifier.size(CellLockSize),
            )
        } else {
            Text(
                text = symbol,
                style = GlyphTextStyle.Mini.copy(fontFamily = cell.font.fontFamily),
                color = colors.onSurface,
            )
        }
    }
}

@Composable
private fun CellsLegend() {
    val colors = MaterialTheme.colorScheme
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        LegendItem(
            FontCellStatus.RECOGNIZED,
            Modifier.background(GeAlphTheme.learningColors.masteryConfident, LegendSwatchShape),
        )
        LegendItem(FontCellStatus.LEARNING, Modifier.border(CellBorderWidth, colors.outline, LegendSwatchShape))
        LegendItem(FontCellStatus.LOCKED, Modifier.background(colors.surfaceContainerHighest, LegendSwatchShape))
    }
}

@Composable
private fun LegendItem(status: FontCellStatus, swatch: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            Modifier
                .size(LegendSwatchSize)
                .then(swatch),
        )
        Text(
            text = stringResource(status.labelRes),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun LearningLevel.labelColor(): Color = when (this) {
    LearningLevel.NEW -> MaterialTheme.colorScheme.onSurfaceVariant
    else -> GeAlphTheme.learningColors.mastery(ordinal)
}

@get:StringRes
private val LearningLevel.labelRes: Int
    get() = when (this) {
        LearningLevel.NEW -> R.string.feature_alphabet_ui_legend_new
        LearningLevel.FAMILIAR -> R.string.feature_alphabet_ui_legend_familiar
        LearningLevel.CONFIDENT -> R.string.feature_alphabet_ui_legend_confident
        LearningLevel.MASTER -> R.string.feature_alphabet_ui_legend_master
    }

@get:StringRes
private val FontCellStatus.labelRes: Int
    get() = when (this) {
        FontCellStatus.RECOGNIZED -> R.string.feature_alphabet_ui_mastery_status_recognized
        FontCellStatus.LEARNING -> R.string.feature_alphabet_ui_mastery_status_learning
        FontCellStatus.LOCKED -> R.string.feature_alphabet_ui_mastery_status_locked
    }

private val PreviewMastery = MasteryUi(
    symbol = "ა",
    level = LearningLevel.CONFIDENT,
    recognizedCount = 3,
    fontsCount = FontCatalog.all.size,
    accuracyPercent = 91,
    cells = FontCatalog.all.mapIndexed { index, font ->
        val status = when {
            index < 3 -> FontCellStatus.RECOGNIZED
            font.unlockedByDefault -> FontCellStatus.LEARNING
            else -> FontCellStatus.LOCKED
        }
        FontCellUi(font, status)
    }.toImmutableList(),
)

@Preview(name = "light", widthDp = 360)
@Composable
private fun MasterySectionLightPreview() {
    GeAlphTheme(darkTheme = false) {
        MasterySection(PreviewMastery, onIntent = {}, Modifier.background(MaterialTheme.colorScheme.background))
    }
}

@Preview(name = "dark", widthDp = 360)
@Composable
private fun MasterySectionDarkPreview() {
    GeAlphTheme(darkTheme = true) {
        MasterySection(PreviewMastery, onIntent = {}, Modifier.background(MaterialTheme.colorScheme.background))
    }
}

@Preview(name = "new letter, light", widthDp = 360)
@Composable
private fun MasterySectionNewPreview() {
    GeAlphTheme(darkTheme = false) {
        MasterySection(
            PreviewMastery.copy(
                level = LearningLevel.NEW,
                recognizedCount = 0,
                accuracyPercent = null,
                cells = PreviewMastery.cells.map {
                    if (it.status == FontCellStatus.RECOGNIZED) it.copy(status = FontCellStatus.LEARNING) else it
                }.toImmutableList(),
            ),
            onIntent = {},
            Modifier.background(MaterialTheme.colorScheme.background),
        )
    }
}
