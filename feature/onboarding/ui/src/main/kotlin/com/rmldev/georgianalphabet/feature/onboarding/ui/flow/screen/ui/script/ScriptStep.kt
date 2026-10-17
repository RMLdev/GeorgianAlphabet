package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.ui.script

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.theme.NotoSansGeorgian
import com.rmldev.georgianalphabet.feature.onboarding.ui.R
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.message.OnboardingIntent
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.ScriptOptionUi
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.ScriptStepUiState
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript

/**
 * Шаг 2, выбор знакомого алфавита (макет Onb2). Рисуется внутри каркаса шагов:
 * шапка и кнопка «Дальше» уже есть, здесь только содержимое.
 */
@Composable
internal fun ScriptStep(
    state: ScriptStepUiState,
    onIntent: (OnboardingIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp), modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(R.string.feature_onboarding_ui_script_title),
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = stringResource(R.string.feature_onboarding_ui_script_body),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.selectableGroup(),
        ) {
            state.options.forEach { option ->
                ScriptOptionCard(
                    option = option,
                    onClick = { onIntent(OnboardingIntent.ScriptSelected(option.script)) },
                )
            }
        }
    }
}

/** Карточка-радиокнопка: плитки с буквами, радиокнопка, название и пример подписи. */
@Composable
private fun ScriptOptionCard(option: ScriptOptionUi, onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    val borderColor by animateColorAsState(
        targetValue = if (option.selected) colors.primary else colors.outlineVariant,
        label = "ScriptOptionBorder",
    )
    val containerColor by animateColorAsState(
        targetValue = if (option.selected) colors.primaryContainer else Color.Transparent,
        label = "ScriptOptionContainer",
    )
    val shape = MaterialTheme.shapes.extraLarge
    val description = stringResource(option.script.descriptionRes)

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .border(width = 2.dp, color = borderColor, shape = shape)
            .background(containerColor)
            .selectable(selected = option.selected, role = Role.RadioButton, onClick = onClick)
            .semantics { contentDescription = description }
            .padding(16.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.clearAndSetSemantics {},
        ) {
            option.glyphs.forEachIndexed { index, glyph ->
                GlyphTile(glyph = glyph, selected = option.selected, shape = tileShape(index))
            }
            Spacer(Modifier.weight(1f))
            RadioButton(selected = option.selected, onClick = null)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .clearAndSetSemantics {},
        ) {
            Text(
                text = stringResource(option.script.labelRes),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = exampleText(option.example),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun GlyphTile(glyph: String, selected: Boolean, shape: Shape) {
    val colors = MaterialTheme.colorScheme
    val tileColor by animateColorAsState(
        targetValue = if (selected) colors.primary else colors.surfaceContainer,
        label = "ScriptGlyphTile",
    )
    val glyphColor by animateColorAsState(
        targetValue = if (selected) colors.onPrimary else colors.onSurface,
        label = "ScriptGlyphText",
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(64.dp)
            .background(tileColor, shape),
    ) {
        Text(
            text = glyph,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Medium,
            color = glyphColor,
        )
    }
}

/** «ანი → ани»: название буквы набрано грузинским шрифтом интерфейса. */
@Composable
private fun exampleText(example: String): AnnotatedString {
    val georgian = stringResource(R.string.feature_onboarding_ui_script_example_georgian)
    val text = stringResource(R.string.feature_onboarding_ui_script_example, georgian, example)
    val start = text.indexOf(georgian)
    return buildAnnotatedString {
        append(text)
        if (start >= 0) addStyle(SpanStyle(fontFamily = NotoSansGeorgian), start, start + georgian.length)
    }
}

/** Формы плиток как в мозаике приветствия: круг, скругление и «капля». */
private val TILE_SHAPES: List<Shape> = listOf(
    CircleShape,
    RoundedCornerShape(20.dp),
    RoundedCornerShape(
        topStart = CornerSize(percent = 50),
        topEnd = CornerSize(percent = 50),
        bottomEnd = CornerSize(percent = 50),
        bottomStart = CornerSize(16.dp),
    ),
)

private fun tileShape(index: Int): Shape = TILE_SHAPES[index % TILE_SHAPES.size]

private val KnownScript.labelRes: Int
    get() = when (this) {
        KnownScript.CYRILLIC -> R.string.feature_onboarding_ui_script_cyrillic
        KnownScript.LATIN -> R.string.feature_onboarding_ui_script_latin
    }

private val KnownScript.descriptionRes: Int
    get() = when (this) {
        KnownScript.CYRILLIC -> R.string.feature_onboarding_ui_script_cyrillic_description
        KnownScript.LATIN -> R.string.feature_onboarding_ui_script_latin_description
    }
