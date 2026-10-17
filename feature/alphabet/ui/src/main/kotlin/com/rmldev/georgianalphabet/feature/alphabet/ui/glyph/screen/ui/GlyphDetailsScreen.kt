package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rmldev.georgianalphabet.core.designsystem.font.FontCatalog
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LearningLevel
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LocalizedText
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsIntent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.ExampleWordUi
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.FontCellStatus
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.FontCellUi
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.GlyphDetailsUiState
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.GlyphStageUi
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.LetterHeaderUi
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.MasteryUi
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.SimilarLetterUi
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.SimilarLettersUi
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.splitByLetter
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section.AssociationSection
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section.CardActions
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section.ExampleWords
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section.GlyphStage
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section.GlyphTopBar
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section.LetterHeader
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section.MasterySection
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section.SimilarLetters
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

private const val STAGE_HEIGHT_FRACTION = 0.75f

/**
 * Карточка буквы: сцена с глифом и секции под ней в одной прокрутке, тулбар поверх.
 * Каждая секция живёт в своём файле `section/` и сама решает, скрыться ли при пустых данных.
 */
@Composable
internal fun GlyphDetailsScreen(state: GlyphDetailsUiState, onIntent: (GlyphDetailsIntent) -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        if (!state.isNotFound) {
            LazyColumn(
                contentPadding = WindowInsets.navigationBars.asPaddingValues(),
                modifier = Modifier.fillMaxSize(),
            ) {
                item(key = "stage") {
                    GlyphStage(
                        symbol = state.symbol,
                        state = state.stage,
                        onIntent = onIntent,
                        modifier = Modifier.fillParentMaxHeight(STAGE_HEIGHT_FRACTION),
                    )
                }
                item(key = "header") { LetterHeader(state.header, onIntent) }
                item(key = "mastery") { MasterySection(state.mastery, onIntent) }
                item(key = "association") { AssociationSection(state.symbol, state.association, onIntent) }
                item(key = "words") { ExampleWords(state.words, onIntent) }
                item(key = "similar") { SimilarLetters(state.similar, onIntent) }
                item(key = "actions") { CardActions(onIntent) }
            }
        }
        GlyphTopBar(symbol = state.symbol, onIntent = onIntent)
    }
}

private val PreviewState = GlyphDetailsUiState(
    letterId = 0,
    symbol = "ა",
    stage = GlyphStageUi(
        fonts = FontCatalog.unlocked().toImmutableList(),
        currentFontIndex = 1,
        lockedFontsCount = FontCatalog.all.size - FontCatalog.unlocked().size,
    ),
    header = LetterHeaderUi(
        nameGeorgian = "ანი",
        name = "ани",
        sound = "а",
        ipa = "ɑ",
        nameAudio = "audio/letters/0.mp3",
    ),
    mastery = MasteryUi(
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
    ),
    association = LocalizedText(
        ru = "Круглый бочок и хвостик сверху, как у арбуза с веточкой.",
        en = "A round belly with a little tail on top, like a watermelon with a stem.",
    ),
    words = persistentListOf(
        ExampleWordUi(
            georgian = "არა",
            parts = "არა".splitByLetter('ა').toImmutableList(),
            transliteration = "ара",
            translation = LocalizedText("нет", "no"),
            audio = null,
            isPlaying = false,
        ),
    ),
    similar = SimilarLettersUi(
        byShape = persistentListOf(SimilarLetterUi(17, "ს", "сани"), SimilarLetterUi(32, "ჰ", "хэ")),
    ),
)

@Preview(name = "light", widthDp = 360, heightDp = 800)
@Composable
private fun GlyphDetailsScreenLightPreview() {
    GeAlphTheme(darkTheme = false) {
        GlyphDetailsScreen(PreviewState, onIntent = {})
    }
}

@Preview(name = "dark", widthDp = 360, heightDp = 800)
@Composable
private fun GlyphDetailsScreenDarkPreview() {
    GeAlphTheme(darkTheme = true) {
        GlyphDetailsScreen(PreviewState, onIntent = {})
    }
}

@Preview(name = "not found", widthDp = 360, heightDp = 400)
@Composable
private fun GlyphDetailsScreenNotFoundPreview() {
    GeAlphTheme(darkTheme = false) {
        GlyphDetailsScreen(GlyphDetailsUiState(isNotFound = true), onIntent = {})
    }
}
