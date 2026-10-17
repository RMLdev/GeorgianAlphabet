package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.ui.welcome

import android.provider.Settings
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rmldev.georgianalphabet.core.designsystem.theme.GlyphTextStyle
import com.rmldev.georgianalphabet.feature.onboarding.ui.R
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.message.OnboardingIntent
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.WelcomeStepUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay

/**
 * Шаг 1, приветствие (макет Main). Занимает весь экран, без каркаса шагов.
 * Мозаика из букв ა в открытых шрифтах: центральная плитка раз в [FONT_CHANGE_PERIOD_MS] меняет шрифт,
 * если в системе не отключены анимации.
 */
@Composable
internal fun WelcomeStep(
    state: WelcomeStepUiState,
    onIntent: (OnboardingIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 28.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp),
        ) {
            GlyphMosaic(
                fonts = state.mosaicFonts,
                modifier = Modifier
                    .widthIn(max = MosaicMaxWidth)
                    .align(Alignment.CenterHorizontally),
            )
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = stringResource(R.string.feature_onboarding_ui_welcome_title),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.semantics { heading() },
                )
                Text(
                    text = stringResource(R.string.feature_onboarding_ui_welcome_body),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Button(
            onClick = { onIntent(OnboardingIntent.StartClicked) },
            enabled = state.buttonsEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        ) {
            Text(stringResource(R.string.feature_onboarding_ui_welcome_start))
        }
        TextButton(
            onClick = { onIntent(OnboardingIntent.SkipClicked) },
            enabled = state.buttonsEnabled,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp),
        ) {
            Text(stringResource(R.string.feature_onboarding_ui_welcome_skip))
        }
    }
}

/** Сетка 3×3 из плиток разной формы; для TalkBack — одно описание на всю мозаику. */
@Composable
private fun GlyphMosaic(fonts: ImmutableList<FontFamily>, modifier: Modifier = Modifier) {
    val animationsEnabled = rememberAnimationsEnabled()
    var tick by remember { mutableIntStateOf(0) }
    LaunchedEffect(animationsEnabled, fonts.size) {
        if (!animationsEnabled || fonts.size < 2) return@LaunchedEffect
        while (true) {
            delay(FONT_CHANGE_PERIOD_MS)
            tick++
        }
    }

    val description = stringResource(R.string.feature_onboarding_ui_welcome_mosaic_description)
    Column(
        verticalArrangement = Arrangement.spacedBy(MosaicGap),
        modifier = modifier.clearAndSetSemantics { contentDescription = description },
    ) {
        repeat(MOSAIC_SIDE) { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(MosaicGap)) {
                repeat(MOSAIC_SIDE) { column ->
                    val index = row * MOSAIC_SIDE + column
                    val isCenter = index == CENTER_INDEX
                    MosaicTile(
                        // Соседние плитки в разных шрифтах, центральная перебирает все по очереди
                        font = if (fonts.isEmpty()) null else fonts[(if (isCenter) tick else index + 1) % fonts.size],
                        shape = TileShapes[index],
                        isCenter = isCenter,
                        alternate = index % 2 == 1,
                        animated = isCenter && animationsEnabled,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun MosaicTile(
    font: FontFamily?,
    shape: Shape,
    isCenter: Boolean,
    alternate: Boolean,
    animated: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    val (background, content) = when {
        isCenter -> colors.primary to colors.onPrimary
        alternate -> colors.primaryContainer to colors.onPrimaryContainer
        else -> colors.surfaceContainer to colors.onSurface
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .aspectRatio(1f)
            .background(background, shape),
    ) {
        if (animated) {
            Crossfade(targetState = font, label = "MosaicCenterFont") { MosaicGlyph(it, isCenter, content) }
        } else {
            MosaicGlyph(font, isCenter, content)
        }
    }
}

@Composable
private fun MosaicGlyph(font: FontFamily?, isCenter: Boolean, color: Color) {
    val style = GlyphTextStyle.Card
    Text(
        text = MOSAIC_GLYPH,
        color = color,
        style = style.copy(
            fontFamily = font ?: style.fontFamily,
            fontSize = if (isCenter) CenterGlyphSize else TileGlyphSize,
            lineHeight = if (isCenter) CenterGlyphSize else TileGlyphSize,
        ),
    )
}

/** «Убрать анимации» в спецвозможностях выставляет масштаб длительности анимаций в 0. */
@Composable
private fun rememberAnimationsEnabled(): Boolean {
    val resolver = LocalContext.current.contentResolver
    return remember(resolver) {
        Settings.Global.getFloat(resolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) != 0f
    }
}

private const val MOSAIC_GLYPH = "ა"
private const val MOSAIC_SIDE = 3
private const val CENTER_INDEX = 4
private const val FONT_CHANGE_PERIOD_MS = 1_300L

private val MosaicGap = 10.dp
private val MosaicMaxWidth = 400.dp
private val TileGlyphSize = 52.sp
private val CenterGlyphSize = 72.sp

// Формы по макету: круги, скругления и «капли» с одним острым углом
private val Drop = RoundedCornerShape(
    topStart = CornerSize(50),
    topEnd = CornerSize(50),
    bottomEnd = CornerSize(50),
    bottomStart = CornerSize(14.dp),
)
private val DropFlipped = RoundedCornerShape(
    topStart = CornerSize(14.dp),
    topEnd = CornerSize(50),
    bottomEnd = CornerSize(50),
    bottomStart = CornerSize(50),
)
private val TileShapes: List<Shape> = listOf(
    CircleShape,
    RoundedCornerShape(28.dp),
    Drop,
    RoundedCornerShape(28.dp),
    RoundedCornerShape(36.dp),
    CircleShape,
    DropFlipped,
    CircleShape,
    RoundedCornerShape(28.dp),
)
