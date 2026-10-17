package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.item.VerticalSpace
import com.rmldev.georgianalphabet.feature.alphabet.ui.R
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetIntent
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetUiState

@Composable
internal fun AlphabetScreen(state: AlphabetUiState, onIntent: (AlphabetIntent) -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp),
        ) {
            VerticalSpace(32.dp)
            Text(
                text = stringResource(R.string.feature_alphabet_ui_screen_name),
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
                        column(1f / state.gridColumnsCount)
                    }
                    repeat(state.gridRowsCount) {
                        row(1.fr)
                    }
                    rowGap(8.dp)
                    columnGap(8.dp)
                },
            ) {
                state.glyphs.forEach { glyph ->
                    LetterTile(
                        glyph = glyph.symbol,
                        name = glyph.name,
                        sound = glyph.sound,
                        progress = glyph.progress,
                        onClick = { onIntent(AlphabetIntent.GlyphClicked(glyph.id)) },
                    )
                }
            }
        }
    }
}
