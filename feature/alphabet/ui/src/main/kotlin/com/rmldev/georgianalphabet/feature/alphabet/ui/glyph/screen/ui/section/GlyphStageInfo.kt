package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.font.GeorgianFont
import com.rmldev.georgianalphabet.feature.alphabet.ui.R

private const val MAX_DIFFICULTY = 3

/** Чип «шрифт + сложность» и счётчик «3 / 7»; на странице закрытых шрифтов — только подпись. */
@Composable
internal fun FontInfoRow(font: GeorgianFont?, position: Int, fontsCount: Int, onFontClick: () -> Unit) {
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
internal fun PageIndicator(pageCount: Int, currentPage: Int, lockedPage: Int?) {
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
