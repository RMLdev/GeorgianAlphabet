package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rmldev.georgianalphabet.core.designsystem.icon.GeAlphIcons
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme
import com.rmldev.georgianalphabet.core.designsystem.theme.NotoSerifGeorgian
import com.rmldev.georgianalphabet.feature.alphabet.ui.R
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsIntent

/** Высота тулбара без статус-бара: сцена отступает на неё сверху и схлопывается до неё. */
internal val GlyphTopBarHeight = 64.dp

/**
 * Тулбар поверх сцены: «Назад к алфавиту», заголовок «Буква ა» и «Все шрифты».
 * Фон совпадает со сценой, поэтому она уезжает под тулбар без шва.
 *
 * @param titleProgress доля проявления заголовка, 0..1; читается на отрисовке, а не в композиции.
 * Заголовок сдвигается снизу, пока проявляется; на 1 под тулбаром появляется разделитель.
 * TalkBack видит заголовок, только когда он проявился больше чем наполовину.
 * @param animationsEnabled false при «Убрать анимации»: без сдвига заголовка и плавного разделителя.
 */
@Composable
internal fun GlyphTopBar(
    symbol: String,
    titleProgress: () -> Float,
    animationsEnabled: Boolean,
    onIntent: (GlyphDetailsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val progress by rememberUpdatedState(titleProgress)
    val isTitleVisible by remember { derivedStateOf { progress() >= TITLE_SEMANTICS_THRESHOLD } }
    val isCollapsed by remember { derivedStateOf { progress() >= 1f } }
    val dividerAlpha by animateFloatAsState(
        targetValue = if (isCollapsed) 1f else 0f,
        animationSpec = if (animationsEnabled) tween(DIVIDER_FADE_MS) else snap(),
        label = "glyphTopBarDivider",
    )
    val dividerColor = MaterialTheme.colorScheme.surfaceContainerHighest

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .bottomDivider(dividerColor) { dividerAlpha }
            .windowInsetsPadding(WindowInsets.statusBars)
            .height(GlyphTopBarHeight)
            .padding(horizontal = 4.dp),
    ) {
        IconButton(onClick = { onIntent(GlyphDetailsIntent.BackClicked) }) {
            Icon(GeAlphIcons.ArrowBack, contentDescription = stringResource(R.string.feature_alphabet_ui_glyph_back))
        }
        Text(
            text = letterTitle(symbol),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .graphicsLayer {
                    val value = progress()
                    alpha = value
                    if (animationsEnabled) translationY = (1f - value) * TitleShift.toPx()
                }
                .then(
                    if (isTitleVisible && symbol.isNotEmpty()) {
                        Modifier.semantics { heading() }
                    } else {
                        Modifier.clearAndSetSemantics {}
                    },
                ),
        )
        IconButton(onClick = { onIntent(GlyphDetailsIntent.AllFontsClicked) }) {
            Icon(
                GeAlphIcons.AllFonts,
                contentDescription = stringResource(R.string.feature_alphabet_ui_glyph_toolbar_all_fonts),
            )
        }
    }
}

/** Линия по нижнему краю; [alpha] читается на отрисовке. */
private fun Modifier.bottomDivider(color: Color, alpha: () -> Float): Modifier = drawBehind {
    val value = alpha()
    if (value > 0f) {
        val y = size.height - DividerThickness.toPx() / 2
        drawLine(
            color = color,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = DividerThickness.toPx(),
            alpha = value,
        )
    }
}

/** «Буква ა»: глиф набран шрифтом-эталоном и чуть крупнее подписи. Пустой, пока буква не загружена. */
@Composable
private fun letterTitle(symbol: String): AnnotatedString {
    if (symbol.isEmpty()) return AnnotatedString("")
    val title = stringResource(R.string.feature_alphabet_ui_glyph_toolbar_title, symbol)
    val start = title.lastIndexOf(symbol)
    return buildAnnotatedString {
        if (start < 0) {
            append(title)
        } else {
            append(title.substring(0, start))
            withStyle(SpanStyle(fontFamily = NotoSerifGeorgian, fontSize = TitleGlyphSize)) { append(symbol) }
            append(title.substring(start + symbol.length))
        }
    }
}

private const val TITLE_SEMANTICS_THRESHOLD = 0.5f
private const val DIVIDER_FADE_MS = 150
private val TitleShift = 8.dp
private val TitleGlyphSize = 26.sp
private val DividerThickness = 1.dp

@Preview(name = "collapsed light")
@Composable
private fun GlyphTopBarCollapsedLightPreview() {
    GeAlphTheme(darkTheme = false) {
        GlyphTopBar(symbol = "ა", titleProgress = { 1f }, animationsEnabled = true, onIntent = {})
    }
}

@Preview(name = "collapsed dark")
@Composable
private fun GlyphTopBarCollapsedDarkPreview() {
    GeAlphTheme(darkTheme = true) {
        GlyphTopBar(symbol = "ა", titleProgress = { 1f }, animationsEnabled = true, onIntent = {})
    }
}

@Preview(name = "expanded")
@Composable
private fun GlyphTopBarExpandedPreview() {
    GeAlphTheme(darkTheme = false) {
        GlyphTopBar(symbol = "ა", titleProgress = { 0f }, animationsEnabled = true, onIntent = {})
    }
}
