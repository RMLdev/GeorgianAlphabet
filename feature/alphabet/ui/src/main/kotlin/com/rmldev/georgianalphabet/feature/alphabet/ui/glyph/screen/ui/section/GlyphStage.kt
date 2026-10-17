package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section

import androidx.annotation.StringRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.font.FontCatalog
import com.rmldev.georgianalphabet.core.designsystem.font.GeorgianFont
import com.rmldev.georgianalphabet.core.designsystem.icon.GeAlphIcons
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphShapeDefaults
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme
import com.rmldev.georgianalphabet.core.designsystem.theme.GlyphTextStyle
import com.rmldev.georgianalphabet.feature.alphabet.ui.R
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsIntent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.GlyphStageUi
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

/** Высота тулбара над сценой: глиф не заходит под кнопки «Назад» и «Все шрифты». */
private val TOOLBAR_HEIGHT = 64.dp
private const val LOCKED_PAGE_KEY = "locked"
private const val MAX_DIFFICULTY = 3

/**
 * Сцена глифа (задача 23): листание открытых шрифтов, последняя страница «Ещё N шрифтов»,
 * стрелки, чип шрифта со сложностью, счётчик и индикатор страниц.
 *
 * Текущий шрифт хранит стор: перелистывание отправляет [GlyphDetailsIntent.FontSelected],
 * а смена [GlyphStageUi.currentFontIndex] извне (загрузка, `fontId` из NavKey, выбор в «Все шрифты»)
 * прокручивает пейджер. Страница закрытых шрифтов шрифтом не считается и в стор не попадает.
 */
@Composable
internal fun GlyphStage(
    symbol: String,
    state: GlyphStageUi,
    onIntent: (GlyphDetailsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hasLockedPage = state.lockedFontsCount > 0
    val pageCount = state.fonts.size + if (hasLockedPage) 1 else 0
    val pagerState = rememberPagerState(initialPage = state.currentFontIndex) { pageCount }
    SyncPagerWithStore(state, pagerState, onIntent)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .clip(GeAlphShapeDefaults.GlyphStage)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(top = TOOLBAR_HEIGHT, start = 8.dp, end = 8.dp, bottom = 20.dp),
    ) {
        PagerWithArrows(
            symbol = symbol,
            state = state,
            pagerState = pagerState,
            onIntent = onIntent,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        )
        Spacer(Modifier.height(16.dp))
        FontInfoRow(
            font = state.fonts.getOrNull(pagerState.currentPage),
            position = pagerState.currentPage + 1,
            fontsCount = state.fonts.size,
            onFontClick = { onIntent(GlyphDetailsIntent.AllFontsClicked) },
        )
        Spacer(Modifier.height(14.dp))
        PageIndicator(
            pageCount = pageCount,
            currentPage = pagerState.currentPage,
            lockedPage = if (hasLockedPage) state.fonts.size else null,
        )
    }
}

/** Страницы шрифтов и закрытых шрифтов между стрелками «Предыдущий / Следующий шрифт». */
@Composable
private fun PagerWithArrows(
    symbol: String,
    state: GlyphStageUi,
    pagerState: PagerState,
    onIntent: (GlyphDetailsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val page = pagerState.currentPage
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        StageArrow(
            icon = GeAlphIcons.ChevronLeft,
            description = R.string.feature_alphabet_ui_glyph_stage_prev_font,
            enabled = page > 0,
            onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.targetPage - 1) } },
        )
        HorizontalPager(
            state = pagerState,
            key = { index -> state.fonts.getOrNull(index)?.id ?: LOCKED_PAGE_KEY },
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        ) { index ->
            val font = state.fonts.getOrNull(index)
            if (font != null) {
                GlyphPage(symbol = symbol, letterName = state.letterName, font = font)
            } else {
                LockedFontsPage(
                    lockedCount = state.lockedFontsCount,
                    onHowToUnlock = { onIntent(GlyphDetailsIntent.AllFontsClicked) },
                )
            }
        }
        StageArrow(
            icon = GeAlphIcons.ChevronRight,
            description = R.string.feature_alphabet_ui_glyph_stage_next_font,
            enabled = page < pagerState.pageCount - 1,
            onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.targetPage + 1) } },
        )
    }
}

/**
 * Пейджер и стор меняют текущий шрифт с двух сторон:
 * - остановка на странице шрифта → [GlyphDetailsIntent.FontSelected], если шрифт сменился;
 * - новый [GlyphStageUi.currentFontIndex] из стора → прокрутка к нему.
 *
 * Последний применённый индекс переживает поворот и уход секции за край списка,
 * поэтому возврат на страницу закрытых шрифтов не сбрасывает её к текущему шрифту.
 */
@Composable
private fun SyncPagerWithStore(
    state: GlyphStageUi,
    pagerState: PagerState,
    onIntent: (GlyphDetailsIntent) -> Unit,
) {
    val fonts by rememberUpdatedState(state.fonts)
    val currentIndex by rememberUpdatedState(state.currentFontIndex)
    val intents by rememberUpdatedState(onIntent)
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { page ->
            val font = fonts.getOrNull(page)
            if (font != null && page != currentIndex) intents(GlyphDetailsIntent.FontSelected(font.id))
        }
    }

    var appliedIndex by rememberSaveable { mutableIntStateOf(state.currentFontIndex) }
    LaunchedEffect(state.currentFontIndex, state.fonts.size) {
        val target = state.currentFontIndex
        if (target != appliedIndex && target in state.fonts.indices) {
            appliedIndex = target
            if (pagerState.settledPage != target) pagerState.scrollToPage(target)
        }
    }
}

@Composable
private fun StageArrow(
    icon: ImageVector,
    @StringRes description: Int,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
    ) {
        Icon(icon, contentDescription = stringResource(description))
    }
}

@Composable
private fun GlyphPage(symbol: String, letterName: String, font: GeorgianFont) {
    val description = stringResource(
        R.string.feature_alphabet_ui_glyph_stage_glyph_description,
        letterName,
        font.displayName,
    )
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Text(
            text = symbol,
            style = GlyphTextStyle.Hero.copy(fontFamily = font.fontFamily),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            modifier = Modifier.clearAndSetSemantics {
                contentDescription = description
                role = Role.Image
            },
        )
    }
}

@Composable
private fun LockedFontsPage(lockedCount: Int, onHowToUnlock: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHighest),
        ) {
            Icon(
                GeAlphIcons.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(40.dp),
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.semantics(mergeDescendants = true) {},
        ) {
            Text(
                text = pluralStringResource(
                    R.plurals.feature_alphabet_ui_glyph_stage_locked_title,
                    lockedCount,
                    lockedCount,
                ),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(R.string.feature_alphabet_ui_glyph_stage_locked_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        FilledTonalButton(onClick = onHowToUnlock) {
            Text(stringResource(R.string.feature_alphabet_ui_glyph_stage_how_to_unlock))
        }
    }
}

/** Чип «шрифт + сложность» и счётчик «3 / 7»; на странице закрытых шрифтов — только подпись. */
@Composable
private fun FontInfoRow(
    font: GeorgianFont?,
    position: Int,
    fontsCount: Int,
    onFontClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .heightIn(min = 32.dp),
    ) {
        if (font != null) {
            FontChip(font, onFontClick)
            Text(
                text = stringResource(R.string.feature_alphabet_ui_glyph_stage_counter, position, fontsCount),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            Text(
                text = stringResource(R.string.feature_alphabet_ui_glyph_stage_counter_locked),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun FontChip(font: GeorgianFont, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.extraSmall,
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .height(32.dp)
                .padding(horizontal = 12.dp),
        ) {
            Text(font.displayName, style = MaterialTheme.typography.labelLarge)
            DifficultyDots(font.difficulty)
        }
    }
}

@Composable
private fun DifficultyDots(difficulty: Int) {
    val description = stringResource(
        when (difficulty) {
            1 -> R.string.feature_alphabet_ui_glyph_stage_difficulty_1
            2 -> R.string.feature_alphabet_ui_glyph_stage_difficulty_2
            else -> R.string.feature_alphabet_ui_glyph_stage_difficulty_3
        },
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        modifier = Modifier.clearAndSetSemantics { contentDescription = description },
    ) {
        repeat(MAX_DIFFICULTY) { index ->
            val color = if (index < difficulty) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
            Box(
                Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(color),
            )
        }
    }
}

/** Точки страниц: активная вытянута, точка страницы закрытых шрифтов темнее остальных. */
@Composable
private fun PageIndicator(pageCount: Int, currentPage: Int, lockedPage: Int?) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.clearAndSetSemantics {},
    ) {
        repeat(pageCount) { index ->
            val active = index == currentPage
            val width by animateDpAsState(if (active) 24.dp else 8.dp, label = "pageDotWidth")
            val color = when {
                active -> MaterialTheme.colorScheme.primary
                index == lockedPage -> MaterialTheme.colorScheme.outline
                else -> MaterialTheme.colorScheme.outlineVariant
            }
            Box(
                Modifier
                    .width(width)
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(color),
            )
        }
    }
}

private val PreviewStage = GlyphStageUi(
    fonts = FontCatalog.unlocked().toImmutableList(),
    currentFontIndex = 1,
    lockedFontsCount = FontCatalog.all.size - FontCatalog.unlocked().size,
    letterName = "ани",
)

@Preview(name = "light", widthDp = 360, heightDp = 588)
@Composable
private fun GlyphStageLightPreview() {
    GeAlphTheme(darkTheme = false) {
        GlyphStage(symbol = "ა", state = PreviewStage, onIntent = {}, modifier = Modifier.fillMaxSize())
    }
}

@Preview(name = "dark", widthDp = 360, heightDp = 588)
@Composable
private fun GlyphStageDarkPreview() {
    GeAlphTheme(darkTheme = true) {
        GlyphStage(symbol = "ა", state = PreviewStage, onIntent = {}, modifier = Modifier.fillMaxSize())
    }
}

@Preview(name = "locked page light", widthDp = 270, heightDp = 300)
@Composable
private fun LockedFontsPageLightPreview() {
    GeAlphTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.surfaceContainer) {
            LockedFontsPage(lockedCount = 8, onHowToUnlock = {})
        }
    }
}

@Preview(name = "locked page dark", widthDp = 270, heightDp = 300)
@Composable
private fun LockedFontsPageDarkPreview() {
    GeAlphTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.surfaceContainer) {
            LockedFontsPage(lockedCount = 8, onHowToUnlock = {})
        }
    }
}
