package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphShapeDefaults
import com.rmldev.georgianalphabet.core.designsystem.theme.GlyphTextStyle
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsIntent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.GlyphDetailsUiState

@Composable
internal fun GlyphDetailsScreen(
    state: GlyphDetailsUiState,
    onIntent: (GlyphDetailsIntent) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .clip(GeAlphShapeDefaults.GlyphStage)
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            Text(
                text = "ა",
                style = GlyphTextStyle.Hero,
            )
        }
    }
}
