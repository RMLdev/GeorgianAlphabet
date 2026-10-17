package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.rmldev.georgianalphabet.core.designsystem.font.FontCatalog
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LearningLevel
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.FontCellStatus
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.FontCellUi
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.MasteryUi
import kotlinx.collections.immutable.toImmutableList

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
