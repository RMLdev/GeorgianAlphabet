package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.item.VerticalSpace
import com.rmldev.georgianalphabet.core.designsystem.theme.GlyphTextStyle
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetIntent
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetUiState
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.GlyphLearningProgress
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.GlyphUi

@Composable
internal fun AlphabetScreen(
    state: AlphabetUiState,
    onIntent: (AlphabetIntent) -> Unit,
    onGlyphClick: (Int) -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp),
        ) {
            VerticalSpace(32.dp)
            Text(
                text = state.screenTitle,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .align(Alignment.Start),
            )
            VerticalSpace(16.dp)

            Grid(
                modifier = Modifier.padding(horizontal = 16.dp),
                config = {
                    repeat(state.gridColumnsCount) {
                        column(0.25f)
                    }
                    repeat(state.gridRowsCount) {
                        row(1.fr)
                    }
                    rowGap(8.dp)
                    columnGap(8.dp)
                },
            ) {
                repeat(state.glyphs.count()) { index ->
                    val glyph = state.glyphs[index]
                    GlyphCard(
                        glyph = state.glyphs[index],
                        onClick = {
                            onGlyphClick(glyph.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
internal fun GlyphCard(
    glyph: GlyphUi,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f / 1.1f)
            .clip(MaterialTheme.shapes.large)
            .background(
                color = when (glyph.progress) {
                    GlyphLearningProgress.NEW -> MaterialTheme.colorScheme.surfaceContainerLow
                    GlyphLearningProgress.STARTED,
                    GlyphLearningProgress.ADVANCED -> MaterialTheme.colorScheme.surfaceContainer

                    GlyphLearningProgress.MASTERED -> MaterialTheme.colorScheme.primaryContainer
                }
            )
            .clickable(true) {
                onClick()
            }
    ) {
        VerticalSpace(10.dp)
        Text(
            text = glyph.symbol.toString(),
            style = GlyphTextStyle.Tile,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        VerticalSpace(4.dp)
        Text(
            text = glyph.name,
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        VerticalSpace(4.dp)
    }
}