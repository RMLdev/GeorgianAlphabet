package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.icon.GeAlphIcons
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme
import com.rmldev.georgianalphabet.core.designsystem.theme.GlyphTextStyle
import com.rmldev.georgianalphabet.feature.alphabet.ui.R
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.GlyphLearningProgress

private val TileHeight = 92.dp
private val MasteryBarWidth = 42.dp
private val MasterySegmentHeight = 4.dp
private val MasterySegmentGap = 3.dp
private val StarSize = 16.dp
private const val MASTERY_SEGMENTS = 3

/**
 * A letter tile of the alphabet grid: the glyph, the "name · [sound]" caption and
 * the 3-segment mastery bar, with a star in the corner once the letter is mastered.
 *
 * @param name Letter name on the alphabet the user knows, like "ани" or "ani".
 * @param sound Letter sound on the same alphabet, like "а" or "gh"; null for archaic
 *        letters, which show only the name.
 * @param glyphStyle Glyph style; pass a copy of [GlyphTextStyle.Tile] with another font
 *        family to draw the glyph in a collection font.
 */
@Composable
internal fun LetterTile(
    glyph: Char,
    name: String,
    sound: String?,
    progress: GlyphLearningProgress,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    glyphStyle: TextStyle = GlyphTextStyle.Tile,
) {
    val level = stringResource(progress.labelRes)
    val description = if (sound != null) {
        stringResource(R.string.feature_alphabet_ui_tile_description, name, sound, level)
    } else {
        stringResource(R.string.feature_alphabet_ui_tile_description_no_sound, name, level)
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(TileHeight)
            .clip(MaterialTheme.shapes.large)
            .background(progress.tileColor())
            .clickable(onClick = onClick)
            .clearAndSetSemantics {
                contentDescription = description
                role = Role.Button
            },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 4.dp),
        ) {
            Text(
                text = glyph.toString(),
                style = glyphStyle,
                color = MaterialTheme.colorScheme.onSurface,
            )
            TileCaption(name = name, sound = sound)
            MasteryBar(progress = progress)
        }
        if (progress == GlyphLearningProgress.MASTERED) {
            Icon(
                imageVector = GeAlphIcons.Star,
                contentDescription = null,
                tint = GeAlphTheme.learningColors.masteryMaster,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(StarSize),
            )
        }
    }
}

@Composable
private fun TileCaption(name: String, sound: String?) {
    val colors = MaterialTheme.colorScheme
    val caption = buildAnnotatedString {
        append(name)
        if (sound != null) {
            withStyle(SpanStyle(color = colors.onSurfaceVariant.copy(alpha = 0.6f))) { append(" · ") }
            withStyle(SpanStyle(color = colors.onSurface, fontWeight = FontWeight.Medium)) {
                append("[$sound]")
            }
        }
    }
    Text(
        text = caption,
        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal),
        color = colors.onSurfaceVariant,
        maxLines = 1,
        softWrap = false,
        overflow = TextOverflow.Clip,
    )
}

@Composable
private fun MasteryBar(progress: GlyphLearningProgress) {
    val colors = GeAlphTheme.learningColors
    val filled = progress.ordinal
    val filledColor = colors.mastery(filled)
    Row(
        horizontalArrangement = Arrangement.spacedBy(MasterySegmentGap),
        modifier = Modifier.width(MasteryBarWidth),
    ) {
        repeat(MASTERY_SEGMENTS) { index ->
            Box(
                Modifier
                    .weight(1f)
                    .height(MasterySegmentHeight)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(if (index < filled) filledColor else colors.masteryEmpty),
            )
        }
    }
}

@Composable
private fun GlyphLearningProgress.tileColor(): Color = when (this) {
    GlyphLearningProgress.NEW -> MaterialTheme.colorScheme.surfaceContainerLow

    GlyphLearningProgress.STARTED,
    GlyphLearningProgress.ADVANCED,
    -> MaterialTheme.colorScheme.surfaceContainer

    GlyphLearningProgress.MASTERED -> MaterialTheme.colorScheme.primaryContainer
}

@get:StringRes
private val GlyphLearningProgress.labelRes: Int
    get() = when (this) {
        GlyphLearningProgress.NEW -> R.string.feature_alphabet_ui_level_new
        GlyphLearningProgress.STARTED -> R.string.feature_alphabet_ui_level_familiar
        GlyphLearningProgress.ADVANCED -> R.string.feature_alphabet_ui_level_confident
        GlyphLearningProgress.MASTERED -> R.string.feature_alphabet_ui_level_master
    }

private data class PreviewLetter(val glyph: Char, val name: String, val sound: String?)

private val CyrillicPreviewLetters = listOf(
    PreviewLetter('ა', "ани", "а"),
    PreviewLetter('ღ', "гхани", "гх"),
    PreviewLetter('წ', "ц’или", "ц’"),
    PreviewLetter('ჭ', "ч’ари", "ч’"),
    PreviewLetter('ჱ', "эй", null),
)

private val LatinPreviewLetters = listOf(
    PreviewLetter('ა', "ani", "a"),
    PreviewLetter('ღ', "ghani", "gh"),
    PreviewLetter('წ', "ts’ili", "ts’"),
    PreviewLetter('ჭ', "ch’ari", "ch’"),
    PreviewLetter('ჱ', "ei", null),
)

@Composable
private fun LetterTilesPreview(letters: List<PreviewLetter>, darkTheme: Boolean) {
    GeAlphTheme(darkTheme = darkTheme) {
        Surface(color = MaterialTheme.colorScheme.background) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(16.dp),
            ) {
                items(GlyphLearningProgress.entries) { progress ->
                    val letter = letters[progress.ordinal]
                    LetterTile(letter.glyph, letter.name, letter.sound, progress, onClick = {})
                }
                items(letters.drop(GlyphLearningProgress.entries.size)) { letter ->
                    LetterTile(letter.glyph, letter.name, letter.sound, GlyphLearningProgress.NEW, onClick = {})
                }
            }
        }
    }
}

@Preview(name = "Cyrillic · light", widthDp = 360)
@Composable
private fun LetterTileCyrillicLightPreview() = LetterTilesPreview(CyrillicPreviewLetters, darkTheme = false)

@Preview(name = "Cyrillic · dark", widthDp = 360)
@Composable
private fun LetterTileCyrillicDarkPreview() = LetterTilesPreview(CyrillicPreviewLetters, darkTheme = true)

@Preview(name = "Latin · light", widthDp = 360)
@Composable
private fun LetterTileLatinLightPreview() = LetterTilesPreview(LatinPreviewLetters, darkTheme = false)

@Preview(name = "Latin · dark", widthDp = 360)
@Composable
private fun LetterTileLatinDarkPreview() = LetterTilesPreview(LatinPreviewLetters, darkTheme = true)
