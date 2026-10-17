package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui

import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.font.FontCatalog
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphShapeDefaults
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
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section.GlyphTopBarHeight
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section.LetterHeader
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section.MasterySection
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section.SimilarLetters
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/**
 * Карточка буквы: сцена с глифом и секции под ней в одной прокрутке, тулбар поверх.
 * Сцена уезжает под тулбар: глиф гаснет и уменьшается, в тулбаре проявляется «Буква ა».
 * Каждая секция живёт в своём файле `section/` и сама решает, скрыться ли при пустых данных.
 */
@Composable
internal fun GlyphDetailsScreen(state: GlyphDetailsUiState, onIntent: (GlyphDetailsIntent) -> Unit) {
    val listState = rememberLazyListState()
    val collapse = rememberStageCollapse(listState)
    val animationsEnabled = rememberAnimationsEnabled()

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        if (!state.isNotFound) {
            LazyColumn(
                state = listState,
                contentPadding = WindowInsets.navigationBars.asPaddingValues(),
                modifier = Modifier.fillMaxSize(),
            ) {
                item(key = "stage") {
                    CollapsingStage(collapse, animationsEnabled) {
                        GlyphStage(
                            symbol = state.symbol,
                            state = state.stage,
                            onIntent = onIntent,
                            modifier = it,
                        )
                    }
                }
                item(key = "header") { LetterHeader(state.header, onIntent) }
                item(key = "mastery") { MasterySection(state.mastery, onIntent) }
                item(key = "association") { AssociationSection(state.symbol, state.association, onIntent) }
                item(key = "words") { ExampleWords(state.words, onIntent) }
                item(key = "similar") { SimilarLetters(state.similar, onIntent) }
                item(key = "actions") { CardActions(onIntent) }
            }
        }
        GlyphTopBar(
            symbol = state.symbol,
            titleProgress = { if (state.isNotFound) 1f else collapse.titleProgress },
            animationsEnabled = animationsEnabled,
            onIntent = onIntent,
        )
    }
}

/**
 * Контейнер сцены: фон, скругление снизу, отступ под тулбар. Ему же принадлежит схлопывание:
 * [content] получает модификатор, который гасит и уменьшает глиф по ходу прокрутки.
 * Фон сцены лежит здесь, поэтому собственный фон секции может гаснуть вместе с глифом без шва.
 */
@Composable
private fun CollapsingStage(
    collapse: StageCollapse,
    animationsEnabled: Boolean,
    content: @Composable (Modifier) -> Unit,
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(StageHeight)
            .clip(GeAlphShapeDefaults.GlyphStage)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(top = GlyphTopBarHeight, bottom = StageBottomPadding),
    ) {
        content(
            Modifier
                .fillMaxSize()
                .graphicsLayer {
                    val progress = collapse.heroProgress
                    alpha = 1f - progress
                    if (animationsEnabled) {
                        val scale = 1f - (1f - HERO_MIN_SCALE) * progress
                        scaleX = scale
                        scaleY = scale
                        transformOrigin = HeroTransformOrigin
                    }
                },
        )
    }
}

/** Путь схлопывания: от верха до момента, когда низ сцены упирается в тулбар. */
@Composable
private fun rememberStageCollapse(listState: LazyListState): StageCollapse {
    val density = LocalDensity.current
    val statusBarTop = WindowInsets.statusBars.getTop(density)
    val distance = with(density) { StageHeight.toPx() - GlyphTopBarHeight.toPx() } - statusBarTop
    return remember(listState, distance) {
        StageCollapse(
            scrolled = {
                if (listState.firstVisibleItemIndex > 0) distance else listState.firstVisibleItemScrollOffset.toFloat()
            },
            distance = distance,
        )
    }
}

/**
 * Схлопывание сцены по макету Letter: глиф гаснет за первые 70% пути [distance],
 * заголовок тулбара проявляется на отрезке 60–90%.
 * [scrolled] читается внутри фаз отрисовки, поэтому прокрутка не вызывает рекомпозицию экрана.
 */
@Stable
internal class StageCollapse(private val scrolled: () -> Float, private val distance: Float) {
    /** 0 — глиф виден целиком, 1 — погас и уменьшен до [HERO_MIN_SCALE]. */
    val heroProgress: Float get() = progress(0f, HERO_FADE_END)

    /** 0 — заголовок скрыт, 1 — виден, под тулбаром появляется разделитель. */
    val titleProgress: Float get() = progress(TITLE_FADE_START, TITLE_FADE_END)

    private fun progress(start: Float, end: Float): Float {
        if (distance <= 0f) return 1f
        return ((scrolled() / distance - start) / (end - start)).coerceIn(0f, 1f)
    }
}

/** «Убрать анимации» в спецвозможностях выставляет масштаб длительности анимаций в 0. */
@Composable
private fun rememberAnimationsEnabled(): Boolean {
    val resolver = LocalContext.current.contentResolver
    return remember(resolver) {
        Settings.Global.getFloat(resolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) != 0f
    }
}

/** Высота сцены вместе со статус-баром, как в макете. */
private val StageHeight = 588.dp
private val StageBottomPadding = 20.dp
private const val HERO_MIN_SCALE = 0.75f
private val HeroTransformOrigin = TransformOrigin(pivotFractionX = 0.5f, pivotFractionY = 0f)
private const val HERO_FADE_END = 0.7f
private const val TITLE_FADE_START = 0.6f
private const val TITLE_FADE_END = 0.9f

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
