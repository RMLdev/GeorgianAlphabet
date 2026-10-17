package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme
import com.rmldev.georgianalphabet.feature.alphabet.ui.R
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetIntent
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetGridItem
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetUiState
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.GlyphLearningProgress
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.GlyphUi
import kotlinx.collections.immutable.toImmutableList

private const val GRID_COLUMNS = 4

@Composable
internal fun AlphabetScreen(state: AlphabetUiState, onIntent: (AlphabetIntent) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(GRID_COLUMNS),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        fullWidthItem(key = "header") {
            AlphabetHeader(lettersCount = state.lettersCount, masteredCount = state.masteredCount)
        }
        alphabetGridItems(state, onIntent)
    }
}

private fun LazyGridScope.fullWidthItem(key: String, content: @Composable () -> Unit) {
    item(key = key, span = { GridItemSpan(maxLineSpan) }) { content() }
}

private fun LazyGridScope.alphabetGridItems(state: AlphabetUiState, onIntent: (AlphabetIntent) -> Unit) {
    items(
        items = state.gridItems,
        key = { it.key },
        span = { item ->
            when (item) {
                is AlphabetGridItem.Glyph -> GridItemSpan(1)
                is AlphabetGridItem.LessonHeader -> GridItemSpan(maxLineSpan)
            }
        },
        contentType = { it::class },
    ) { item ->
        when (item) {
            is AlphabetGridItem.Glyph -> LetterTile(
                glyph = item.glyph.symbol,
                name = item.glyph.name,
                sound = item.glyph.sound,
                progress = item.glyph.progress,
                onClick = { onIntent(AlphabetIntent.GlyphClicked(item.glyph.id)) },
            )

            is AlphabetGridItem.LessonHeader -> LessonHeader(item)
        }
    }
}

@Composable
private fun AlphabetHeader(lettersCount: Int, masteredCount: Int) {
    Column(Modifier.padding(start = 4.dp, end = 4.dp, bottom = 8.dp)) {
        Text(
            text = stringResource(R.string.feature_alphabet_ui_screen_name),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.semantics { heading() },
        )
        Text(
            text = pluralStringResource(
                R.plurals.feature_alphabet_ui_subtitle,
                lettersCount,
                lettersCount,
                masteredCount,
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun LessonHeader(header: AlphabetGridItem.LessonHeader) {
    Column(Modifier.padding(start = 4.dp, end = 4.dp, top = 8.dp)) {
        Text(
            text = stringResource(R.string.feature_alphabet_ui_lesson_title, header.lesson),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.semantics { heading() },
        )
        Text(
            text = stringResource(
                R.string.feature_alphabet_ui_lesson_progress,
                header.masteredCount,
                header.lettersCount,
            ),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private val PreviewState = AlphabetUiState(
    lettersCount = 33,
    masteredCount = 1,
    gridItems = listOf(
        AlphabetGridItem.LessonHeader(lesson = 1, masteredCount = 1, lettersCount = 6),
        AlphabetGridItem.Glyph(GlyphUi(0, 'ა', "ани", "а", GlyphLearningProgress.MASTERED, 1)),
        AlphabetGridItem.Glyph(GlyphUi(1, 'ბ', "бани", "б", GlyphLearningProgress.ADVANCED, 1)),
        AlphabetGridItem.Glyph(GlyphUi(2, 'გ', "гани", "г", GlyphLearningProgress.STARTED, 1)),
        AlphabetGridItem.Glyph(GlyphUi(3, 'დ', "дони", "д", GlyphLearningProgress.NEW, 1)),
        AlphabetGridItem.Glyph(GlyphUi(4, 'ე', "эни", "э", GlyphLearningProgress.NEW, 1)),
        AlphabetGridItem.Glyph(GlyphUi(5, 'ვ', "вини", "в", GlyphLearningProgress.NEW, 1)),
    ).toImmutableList(),
)

@Preview(name = "light", widthDp = 360, heightDp = 480)
@Composable
private fun AlphabetScreenLightPreview() = GeAlphTheme(darkTheme = false) {
    AlphabetScreen(PreviewState, onIntent = {})
}

@Preview(name = "dark", widthDp = 360, heightDp = 480)
@Composable
private fun AlphabetScreenDarkPreview() = GeAlphTheme(darkTheme = true) {
    AlphabetScreen(PreviewState, onIntent = {})
}
