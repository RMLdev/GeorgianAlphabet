package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.font.FontCatalog
import com.rmldev.georgianalphabet.core.designsystem.font.label
import com.rmldev.georgianalphabet.core.designsystem.icon.GeAlphIcons
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme
import com.rmldev.georgianalphabet.core.designsystem.theme.GlyphTextStyle
import com.rmldev.georgianalphabet.feature.alphabet.ui.R
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.state.FontTileUi

private val TileHeight = 136.dp
private val DashWidth = 1.5.dp
private val DashOn = 6.dp
private val DashOff = 4.dp
private val CheckBadgeSize = 20.dp
private val CheckIconSize = 12.dp
private val LockIconSize = 26.dp
private val HintHeight = 20.dp

/** Плитка шрифта в сетке «Все шрифты»: открытый — буква в этом шрифте, закрытый — замок и условие открытия. */
@Composable
internal fun FontTile(
    symbol: String,
    letterName: String,
    tile: FontTileUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (tile.isOpen) {
        OpenFontTile(symbol, letterName, tile, onClick, modifier)
    } else {
        LockedFontTile(tile, modifier)
    }
}

@Composable
private fun OpenFontTile(
    symbol: String,
    letterName: String,
    tile: FontTileUi,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    val name = letterName.replaceFirstChar { it.uppercase() }
    val description = if (tile.isRecognized) {
        stringResource(R.string.feature_alphabet_ui_all_fonts_font_recognized, name, tile.font.displayName)
    } else {
        stringResource(R.string.feature_alphabet_ui_all_fonts_font_open, name, tile.font.displayName)
    }
    Box(
        modifier
            .height(TileHeight)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable(
                onClickLabel = stringResource(R.string.feature_alphabet_ui_all_fonts_open_font),
                role = Role.Button,
                onClick = onClick,
            )
            .semantics { contentDescription = description },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .padding(start = 8.dp, top = 10.dp, end = 8.dp, bottom = 12.dp)
                .clearAndSetSemantics {},
        ) {
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    text = symbol,
                    style = GlyphTextStyle.Card.copy(fontFamily = tile.font.fontFamily),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            FontName(tile.font.displayName)
        }
        if (tile.isRecognized) {
            RecognizedBadge(
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
            )
        }
    }
}

@Composable
private fun RecognizedBadge(modifier: Modifier = Modifier) {
    val colors = GeAlphTheme.learningColors
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(CheckBadgeSize)
            .background(colors.masteryConfident, CircleShape),
    ) {
        Icon(
            GeAlphIcons.Check,
            contentDescription = null,
            tint = colors.onMasteryConfident,
            modifier = Modifier.size(CheckIconSize),
        )
    }
}

@Composable
private fun LockedFontTile(tile: FontTileUi, modifier: Modifier) {
    val condition = tile.font.unlockCondition?.label().orEmpty()
    val description = stringResource(
        R.string.feature_alphabet_ui_all_fonts_font_locked,
        tile.font.displayName,
        condition,
    )
    val borderColor = MaterialTheme.colorScheme.outline
    val shape = MaterialTheme.shapes.large
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
            .height(TileHeight)
            .drawBehind {
                val stroke = DashWidth.toPx()
                val radius = shape.topStart.toPx(size, this)
                drawRoundRect(
                    color = borderColor,
                    topLeft = Offset(stroke / 2, stroke / 2),
                    size = Size(size.width - stroke, size.height - stroke),
                    cornerRadius = CornerRadius(radius),
                    style = Stroke(
                        width = stroke,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(DashOn.toPx(), DashOff.toPx())),
                    ),
                )
            }
            .clearAndSetSemantics { contentDescription = description }
            .padding(start = 8.dp, top = 10.dp, end = 8.dp, bottom = 12.dp),
    ) {
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Icon(
                GeAlphIcons.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(LockIconSize),
            )
        }
        FontName(tile.font.displayName)
        if (condition.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(HintHeight)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh, CircleShape)
                    .padding(horizontal = 8.dp),
            ) {
                Text(
                    text = condition,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun FontName(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
    )
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FontTilePreview() {
    GeAlphTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
        ) {
            val tileModifier = Modifier.width(110.dp)
            val recognized = FontTileUi(FontCatalog.all[1], isOpen = true, isRecognized = true)
            FontTile("ა", "ани", recognized, {}, tileModifier)
            FontTile("ა", "ани", FontTileUi(FontCatalog.all[2], isOpen = true), {}, tileModifier)
            FontTile("ა", "ани", FontTileUi(FontCatalog.all.last(), isOpen = false), {}, tileModifier)
        }
    }
}
