package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.font.FontCatalog
import com.rmldev.georgianalphabet.core.designsystem.icon.GeAlphIcons
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme
import com.rmldev.georgianalphabet.core.designsystem.theme.GlyphTextStyle
import com.rmldev.georgianalphabet.feature.alphabet.ui.R
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.message.AllFontsIntent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.state.AllFontsUiState
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.state.FontGroupUi
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.state.FontTileUi
import kotlinx.collections.immutable.toImmutableList

private const val COLUMNS = 3
private const val MAX_DIFFICULTY = 3
private val LetterTileSize = 56.dp
private val ProgressHeight = 6.dp
private val DotSize = 6.dp

/** «Все шрифты»: буква во всех шрифтах коллекции, сгруппированных по сложности. */
@Composable
internal fun AllFontsScreen(state: AllFontsUiState, onIntent: (AllFontsIntent) -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        IconButton(
            onClick = { onIntent(AllFontsIntent.BackClicked) },
            modifier = Modifier.padding(start = 4.dp, top = 4.dp),
        ) {
            Icon(
                GeAlphIcons.ArrowBack,
                contentDescription = stringResource(R.string.feature_alphabet_ui_all_fonts_back),
            )
        }
        if (state.symbol.isNotEmpty()) {
            val navigationBars = WindowInsets.navigationBars.asPaddingValues()
            LazyVerticalGrid(
                columns = GridCells.Fixed(COLUMNS),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 32.dp + navigationBars.calculateBottomPadding(),
                ),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                item(key = "header", span = { GridItemSpan(maxLineSpan) }) {
                    AllFontsHeader(state, Modifier.padding(start = 4.dp, end = 4.dp, top = 12.dp, bottom = 8.dp))
                }
                state.groups.forEach { group -> fontGroup(state, group, onIntent) }
            }
        }
    }
}

private fun LazyGridScope.fontGroup(state: AllFontsUiState, group: FontGroupUi, onIntent: (AllFontsIntent) -> Unit) {
    item(key = "group_${group.difficulty}", span = { GridItemSpan(maxLineSpan) }) {
        FontGroupHeader(group, Modifier.padding(start = 4.dp, end = 4.dp, top = 16.dp, bottom = 2.dp))
    }
    items(group.fonts, key = { it.font.id }) { tile ->
        FontTile(
            symbol = state.symbol,
            letterName = state.letterName,
            tile = tile,
            onClick = { onIntent(AllFontsIntent.FontClicked(tile.font.id)) },
        )
    }
}

@Composable
private fun AllFontsHeader(state: AllFontsUiState, modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(LetterTileSize)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.primaryContainer),
            ) {
                Text(
                    text = state.symbol,
                    style = GlyphTextStyle.Tile,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = stringResource(R.string.feature_alphabet_ui_all_fonts_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.semantics { heading() },
                )
                Text(
                    text = stringResource(
                        R.string.feature_alphabet_ui_all_fonts_subtitle,
                        state.letterName,
                        state.openCount,
                        state.totalCount,
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        val progressDescription = stringResource(R.string.feature_alphabet_ui_all_fonts_progress)
        LinearProgressIndicator(
            progress = { if (state.totalCount == 0) 0f else state.openCount.toFloat() / state.totalCount },
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            strokeCap = StrokeCap.Round,
            gapSize = 0.dp,
            drawStopIndicator = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(ProgressHeight)
                .semantics { contentDescription = progressDescription },
        )
    }
}

@Composable
private fun FontGroupHeader(group: FontGroupUi, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier,
    ) {
        DifficultyDots(group.difficulty)
        Text(
            text = stringResource(group.titleRes()),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.semantics { heading() },
        )
        Text(
            text = stringResource(
                R.string.feature_alphabet_ui_all_fonts_group_count,
                group.openCount,
                group.fonts.size,
            ),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/** Сложность точками: закрашено [difficulty] из трёх. Дублирует название группы, поэтому без описания. */
@Composable
private fun DifficultyDots(difficulty: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        repeat(MAX_DIFFICULTY) { index ->
            val color = if (index < difficulty) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
            Box(
                Modifier
                    .size(DotSize)
                    .background(color, CircleShape),
            )
        }
    }
}

private fun FontGroupUi.titleRes(): Int = when (difficulty) {
    1 -> R.string.feature_alphabet_ui_all_fonts_group_simple
    2 -> R.string.feature_alphabet_ui_all_fonts_group_unusual
    else -> R.string.feature_alphabet_ui_all_fonts_group_decorative
}

private val PreviewState: AllFontsUiState = run {
    val recognized = setOf("noto_serif_georgian", "dejavu_sans", "rail_model", "quivira")
    val tiles = FontCatalog.all.map { font ->
        val isOpen = font.unlockedByDefault || font.id == "quivira"
        FontTileUi(font, isOpen, isRecognized = isOpen && font.id in recognized)
    }
    AllFontsUiState(
        letterId = 0,
        symbol = "ა",
        letterName = "ани",
        openCount = tiles.count { it.isOpen },
        totalCount = tiles.size,
        groups = tiles.groupBy { it.font.difficulty }
            .map { (difficulty, fonts) -> FontGroupUi(difficulty, fonts.count { it.isOpen }, fonts.toImmutableList()) }
            .toImmutableList(),
    )
}

@Preview(name = "light", widthDp = 390, heightDp = 1220)
@Composable
private fun AllFontsScreenLightPreview() {
    GeAlphTheme(darkTheme = false) {
        AllFontsScreen(PreviewState, onIntent = {})
    }
}

@Preview(name = "dark", widthDp = 390, heightDp = 1220)
@Composable
private fun AllFontsScreenDarkPreview() {
    GeAlphTheme(darkTheme = true) {
        AllFontsScreen(PreviewState, onIntent = {})
    }
}
