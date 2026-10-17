package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.ui.name

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.item.GeAlphOutlinedTextField
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme
import com.rmldev.georgianalphabet.core.designsystem.theme.GlyphTextStyle
import com.rmldev.georgianalphabet.core.designsystem.theme.NotoSerifGeorgian
import com.rmldev.georgianalphabet.feature.onboarding.api.model.TransliteratedFragment
import com.rmldev.georgianalphabet.feature.onboarding.ui.R
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.message.OnboardingIntent
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.NameFragmentUi
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.NameStepUiState
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.OnboardingState
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.OnboardingStep
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.toNameStepUi
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript
import kotlinx.collections.immutable.ImmutableList

/**
 * Шаг 3, имя по-грузински (макеты Onb3, Onb4). Рисуется внутри каркаса шагов:
 * шапка с «Пропустить» и кнопка «Начать учить» уже есть, здесь только содержимое.
 * Пока поле пустое — пунктирная заглушка, при вводе — имя мхедрули, чипы разбора и счётчик букв.
 * IME action Done делает то же, что «Начать учить».
 */
@Composable
internal fun NameStep(state: NameStepUiState, onIntent: (OnboardingIntent) -> Unit, modifier: Modifier = Modifier) {
    val fieldState = rememberTextFieldState(initialText = state.name)
    val currentOnIntent by rememberUpdatedState(onIntent)
    LaunchedEffect(fieldState) {
        snapshotFlow { fieldState.text.toString() }
            .collect { currentOnIntent(OnboardingIntent.NameChanged(it)) }
    }

    Column(verticalArrangement = Arrangement.spacedBy(24.dp), modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(R.string.feature_onboarding_ui_name_title),
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = stringResource(R.string.feature_onboarding_ui_name_body),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        GeAlphOutlinedTextField(
            state = fieldState,
            labelText = stringResource(R.string.feature_onboarding_ui_name_label),
            placeholderText = stringResource(
                if (state.knownScript == KnownScript.LATIN) {
                    R.string.feature_onboarding_ui_name_placeholder_latin
                } else {
                    R.string.feature_onboarding_ui_name_placeholder_cyrillic
                },
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrectEnabled = false,
                imeAction = ImeAction.Done,
            ),
            onKeyboardAction = { currentOnIntent(OnboardingIntent.FinishClicked) },
            modifier = Modifier.fillMaxWidth(),
        )
        AnimatedContent(
            targetState = state.georgianName.isNotEmpty(),
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "NameResult",
        ) { hasName ->
            if (hasName) {
                GeorgianNameCard(
                    georgianName = state.georgianName,
                    fragments = state.fragments,
                    uniqueLetterCount = state.uniqueLetterCount,
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                NamePlaceholder(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun GeorgianNameCard(
    georgianName: String,
    fragments: ImmutableList<NameFragmentUi>,
    uniqueLetterCount: Int,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = modifier,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(20.dp)) {
            Text(
                text = stringResource(R.string.feature_onboarding_ui_name_result_title),
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                text = georgianName,
                style = MaterialTheme.typography.displayMedium.copy(fontFamily = NotoSerifGeorgian),
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                fragments.forEach { fragment -> FragmentChip(fragment) }
            }
            Text(
                text = pluralStringResource(
                    R.plurals.feature_onboarding_ui_name_letters_count,
                    uniqueLetterCount,
                    uniqueLetterCount,
                ),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

/** Чип разбора: сверху исходный фрагмент, снизу грузинские буквы. */
@Composable
private fun FragmentChip(fragment: NameFragmentUi) {
    val description = stringResource(
        R.string.feature_onboarding_ui_name_fragment_description,
        fragment.source,
        fragment.georgian,
    )
    Surface(
        shape = MaterialTheme.shapes.small,
        color = chipColor(),
        modifier = Modifier
            .sizeIn(minWidth = 36.dp)
            .height(52.dp)
            .clearAndSetSemantics { contentDescription = description },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
            modifier = Modifier.padding(horizontal = 6.dp),
        ) {
            Text(
                text = fragment.source,
                style = MaterialTheme.typography.bodySmall,
                color = LocalContentColor.current.copy(alpha = 0.8f),
            )
            Text(text = fragment.georgian, style = GlyphTextStyle.Mini)
        }
    }
}

@Composable
private fun NamePlaceholder(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(132.dp)
            .dashedBorder(color = MaterialTheme.colorScheme.outline, cornerRadius = PlaceholderCornerRadius)
            .padding(20.dp),
    ) {
        Text(
            text = stringResource(R.string.feature_onboarding_ui_name_placeholder_result),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * Фон чипа на primaryContainer: в светлой теме полупрозрачный белый, в тёмной — лёгкий
 * налёт onPrimaryContainer, как в макетах Onb4 и Onb4Dark.
 */
@Composable
private fun chipColor(): Color {
    val colors = MaterialTheme.colorScheme
    return if (colors.primaryContainer.luminance() > 0.5f) {
        colors.surfaceContainerLowest.copy(alpha = 0.6f)
    } else {
        colors.onPrimaryContainer.copy(alpha = 0.14f)
    }
}

private fun Modifier.dashedBorder(color: Color, cornerRadius: Dp): Modifier = drawBehind {
    val stroke = DashedBorderWidth.toPx()
    val inset = stroke / 2
    drawRoundRect(
        color = color,
        topLeft = Offset(inset, inset),
        size = Size(size.width - stroke, size.height - stroke),
        cornerRadius = CornerRadius(cornerRadius.toPx() - inset),
        style = Stroke(
            width = stroke,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(DashLength.toPx(), DashGap.toPx())),
        ),
    )
}

// Как MaterialTheme.shapes.extraLarge: рамку рисуем сами, потому что она пунктирная
private val PlaceholderCornerRadius = 28.dp
private val DashedBorderWidth = 1.5.dp
private val DashLength = 6.dp
private val DashGap = 4.dp

private fun previewState(name: String, fragments: List<Pair<String, String>>): NameStepUiState = OnboardingState(
    step = OnboardingStep.NAME,
    knownScript = KnownScript.CYRILLIC,
    userName = name,
    nameFragments = fragments.map { (source, georgian) -> TransliteratedFragment(source, georgian) },
).toNameStepUi()

private val PreviewName = listOf("Р" to "რ", "о" to "ო", "м" to "მ", "а" to "ა", "н" to "ნ")

@Composable
private fun NameStepPreview(state: NameStepUiState, darkTheme: Boolean) {
    GeAlphTheme(darkTheme = darkTheme) {
        Surface(color = MaterialTheme.colorScheme.background) {
            NameStep(state = state, onIntent = {}, modifier = Modifier.padding(24.dp))
        }
    }
}

@Preview(name = "Empty · light", widthDp = 390)
@Composable
private fun NameStepEmptyLightPreview() = NameStepPreview(previewState("", emptyList()), darkTheme = false)

@Preview(name = "Empty · dark", widthDp = 390)
@Composable
private fun NameStepEmptyDarkPreview() = NameStepPreview(previewState("", emptyList()), darkTheme = true)

@Preview(name = "Filled · light", widthDp = 390)
@Composable
private fun NameStepFilledLightPreview() = NameStepPreview(previewState("Роман", PreviewName), darkTheme = false)

@Preview(name = "Filled · dark", widthDp = 390)
@Composable
private fun NameStepFilledDarkPreview() = NameStepPreview(previewState("Роман", PreviewName), darkTheme = true)
