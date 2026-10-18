package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.icon.GeAlphIcons
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme
import com.rmldev.georgianalphabet.feature.alphabet.ui.R

@Composable
internal fun LockedFontsPage(lockedCount: Int, onHowToUnlock: () -> Unit) {
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
