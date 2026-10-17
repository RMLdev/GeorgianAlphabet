package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
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
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.GlyphStageUi

/**
 * Сцена глифа (задача 23): листание открытых шрифтов, страница «Ещё N шрифтов», чип шрифта.
 * Заглушка: глиф в текущем шрифте. Строки — в `strings_glyph_stage.xml`.
 */
@Suppress("UnusedParameter") // onIntent: FontSelected and AllFontsClicked come with the pager
@Composable
internal fun GlyphStage(
    symbol: String,
    state: GlyphStageUi,
    onIntent: (GlyphDetailsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val font = state.fonts.getOrNull(state.currentFontIndex)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .clip(GeAlphShapeDefaults.GlyphStage)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        Text(
            text = symbol,
            style = if (font == null) GlyphTextStyle.Hero else GlyphTextStyle.Hero.copy(fontFamily = font.fontFamily),
        )
    }
}
