package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.icon.GeAlphIcons
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme
import com.rmldev.georgianalphabet.feature.alphabet.ui.R

private val ButtonSize = 48.dp
private val CheckedCorner = 16.dp
private val UncheckedCorner = ButtonSize / 2
private val IconSize = 24.dp

/**
 * Toggle in the alphabet header: while checked, every tile draws its glyph in a random open font.
 * Like in the mockup, it is a circle on surfaceContainerHigh when off and a rounded square on primary when on.
 */
@Composable
internal fun ShuffleFontsButton(checked: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val corner by animateDpAsState(if (checked) CheckedCorner else UncheckedCorner, label = "shuffleCorner")
    val container by animateColorAsState(
        if (checked) colors.primary else colors.surfaceContainerHigh,
        label = "shuffleContainer",
    )
    val content by animateColorAsState(
        if (checked) colors.onPrimary else colors.onSurfaceVariant,
        label = "shuffleContent",
    )
    val description = stringResource(R.string.feature_alphabet_ui_shuffle_fonts)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(ButtonSize)
            .clip(RoundedCornerShape(corner))
            .background(container)
            .toggleable(value = checked, role = Role.Switch, onValueChange = { onClick() })
            .semantics { contentDescription = description },
    ) {
        Icon(
            imageVector = GeAlphIcons.Shuffle,
            contentDescription = null,
            tint = content,
            modifier = Modifier.size(IconSize),
        )
    }
}

@Composable
private fun ShuffleFontsButtonPreview(darkTheme: Boolean) {
    GeAlphTheme(darkTheme = darkTheme) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Row(Modifier.padding(16.dp)) {
                ShuffleFontsButton(checked = false, onClick = {})
                ShuffleFontsButton(checked = true, onClick = {}, modifier = Modifier.padding(start = 16.dp))
            }
        }
    }
}

@Preview(name = "light")
@Composable
private fun ShuffleFontsButtonLightPreview() = ShuffleFontsButtonPreview(darkTheme = false)

@Preview(name = "dark")
@Composable
private fun ShuffleFontsButtonDarkPreview() = ShuffleFontsButtonPreview(darkTheme = true)
