package com.rmldev.georgianalphabet.core.designsystem.theme

import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.rmldev.georgianalphabet.core.designsystem.R

/**
 * Noto Serif Georgian Regular, bundled in res/font (SIL OFL 1.1, see NOTO_SERIF_GEORGIAN_OFL.txt).
 * The standard face of learning glyphs: the alphabet grid, archaic letters, the first page of
 * the letter card and the "standard" glyph next to the right answer.
 */
val NotoSerifGeorgian = FontFamily(Font(R.font.noto_serif_georgian_regular, FontWeight.Normal))

/**
 * Text styles of learning glyphs, one per [GlyphSize].
 * The line height equals the font size and the font padding is off, so the glyph sits in a
 * square box and can be centered there. Pass another [FontFamily] with
 * `style.copy(fontFamily = ...)` to draw the glyph in a collection font.
 * The color is not set: give it with `color =` on `Text`, usually `onSurface`.
 */
object GlyphTextStyle {
    val Mini = glyphStyle(GlyphSize.Mini)
    val Tile = glyphStyle(GlyphSize.Tile)
    val Card = glyphStyle(GlyphSize.Card)
    val Task = glyphStyle(GlyphSize.Task)
    val Hero = glyphStyle(GlyphSize.Hero)
}

private fun glyphStyle(size: TextUnit) = TextStyle(
    fontFamily = NotoSerifGeorgian,
    fontWeight = FontWeight.Normal,
    fontSize = size,
    lineHeight = size,
    textAlign = TextAlign.Center,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.Both,
    ),
)
