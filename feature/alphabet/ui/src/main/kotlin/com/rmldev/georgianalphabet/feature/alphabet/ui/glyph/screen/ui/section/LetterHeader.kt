package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rmldev.georgianalphabet.core.designsystem.icon.GeAlphIcons
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme
import com.rmldev.georgianalphabet.core.designsystem.theme.NotoSansGeorgian
import com.rmldev.georgianalphabet.feature.alphabet.ui.R
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsIntent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.LetterHeaderUi
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript

private val SoundButtonSize = 72.dp

// Rounded square at rest, circle while the name is playing
private val SoundButtonIdleCorner = 24.dp
private val SoundButtonPlayingCorner = SoundButtonSize / 2
private const val SOUND_COLOR_DURATION_MS = 200

/**
 * Название, транслит, МФА и кнопка звука (задача 25).
 * Строки — в `strings_glyph_header.xml`.
 */
@Composable
internal fun LetterHeader(
    state: LetterHeaderUi,
    onIntent: (GlyphDetailsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = state.nameGeorgian,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontFamily = NotoSansGeorgian,
                        fontSize = 40.sp,
                        lineHeight = 52.sp,
                        letterSpacing = 0.sp,
                        localeList = LocaleList("ka"),
                    ),
                )
                Text(
                    text = state.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            // Archaic letters have no recorded name (task 21 voices only the 33 modern ones)
            if (!state.archaic) {
                SoundButton(
                    name = state.name,
                    isPlaying = state.isNamePlaying,
                    onClick = { onIntent(GlyphDetailsIntent.PlayName) },
                )
            }
        }
        ReadingChips(state)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ReadingChips(state: LetterHeaderUi) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (state.sound.isNotEmpty()) {
            val scriptLabel = when (state.knownScript) {
                KnownScript.CYRILLIC -> R.string.feature_alphabet_ui_glyph_header_cyrillic
                KnownScript.LATIN -> R.string.feature_alphabet_ui_glyph_header_latin
            }
            ReadingChip(label = stringResource(scriptLabel), value = state.sound)
        }
        if (state.ipa.isNotEmpty()) {
            ReadingChip(
                label = stringResource(R.string.feature_alphabet_ui_glyph_header_ipa),
                value = stringResource(R.string.feature_alphabet_ui_glyph_header_ipa_value, state.ipa),
            )
        }
    }
}

@Composable
private fun SoundButton(name: String, isPlaying: Boolean, onClick: () -> Unit) {
    val corner by animateDpAsState(
        targetValue = if (isPlaying) SoundButtonPlayingCorner else SoundButtonIdleCorner,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow),
        label = "soundCorner",
    )
    val colors = MaterialTheme.colorScheme
    val container by animateColorAsState(
        targetValue = if (isPlaying) colors.primary else colors.primaryContainer,
        animationSpec = tween(SOUND_COLOR_DURATION_MS),
        label = "soundContainer",
    )
    val content by animateColorAsState(
        targetValue = if (isPlaying) colors.onPrimary else colors.onPrimaryContainer,
        animationSpec = tween(SOUND_COLOR_DURATION_MS),
        label = "soundContent",
    )
    val description = stringResource(R.string.feature_alphabet_ui_glyph_header_listen, name)
    val playingState = stringResource(R.string.feature_alphabet_ui_glyph_header_playing)
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(corner),
        color = container,
        contentColor = content,
        modifier = Modifier
            .size(SoundButtonSize)
            .semantics {
                contentDescription = description
                if (isPlaying) stateDescription = playingState
            },
    ) {
        Icon(
            imageVector = GeAlphIcons.SoundLoud,
            contentDescription = null,
            modifier = Modifier
                .padding(21.dp)
                .size(30.dp),
        )
    }
}

/** Чип «кириллица а» / «МФА [ɑ]»: подпись приглушённым цветом и значение жирным. */
@Composable
private fun ReadingChip(label: String, value: String) {
    Surface(
        shape = MaterialTheme.shapes.extraSmall,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier.clearAndSetSemantics { contentDescription = "$label $value" },
    ) {
        Row(
            modifier = Modifier
                .height(32.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

private val PreviewHeader = LetterHeaderUi(
    nameGeorgian = "ანი",
    name = "ани",
    sound = "а",
    ipa = "ɑ",
    nameAudio = "audio/letters/0.mp3",
)

@Preview(name = "light")
@Composable
private fun LetterHeaderLightPreview() {
    GeAlphTheme(darkTheme = false) {
        Surface { LetterHeader(PreviewHeader, onIntent = {}) }
    }
}

@Preview(name = "dark")
@Composable
private fun LetterHeaderDarkPreview() {
    GeAlphTheme(darkTheme = true) {
        Surface { LetterHeader(PreviewHeader, onIntent = {}) }
    }
}

@Preview(name = "playing, latin")
@Composable
private fun LetterHeaderPlayingPreview() {
    GeAlphTheme(darkTheme = false) {
        Surface {
            LetterHeader(
                PreviewHeader.copy(name = "ani", sound = "a", knownScript = KnownScript.LATIN, isNamePlaying = true),
                onIntent = {},
            )
        }
    }
}

@Preview(name = "archaic, dark")
@Composable
private fun LetterHeaderArchaicPreview() {
    GeAlphTheme(darkTheme = true) {
        Surface {
            LetterHeader(
                LetterHeaderUi(nameGeorgian = "ჲ", name = "йоти", sound = "й", ipa = "j", archaic = true),
                onIntent = {},
            )
        }
    }
}
