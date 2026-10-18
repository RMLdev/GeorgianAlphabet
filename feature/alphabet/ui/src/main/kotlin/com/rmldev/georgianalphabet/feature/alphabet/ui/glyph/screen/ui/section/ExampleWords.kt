package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.section

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.icon.GeAlphIcons
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme
import com.rmldev.georgianalphabet.core.designsystem.theme.NotoSansGeorgian
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LocalizedText
import com.rmldev.georgianalphabet.feature.alphabet.ui.R
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsIntent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.ExampleWordUi
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.WordPart
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.localized
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/**
 * «Примеры слов»: слово мхедрули с подсветкой текущей буквы, «транслит · перевод» и кнопка
 * «Послушать слово» (`PlayWord`), во время проигрывания залитая primary. Пустой список — блок скрыт.
 *
 * Кнопка есть и у слов без [ExampleWordUi.audio]: файл слова по `georgian` находит Actor,
 * а если файла нет, проигрывание молча не происходит.
 */
@Composable
internal fun ExampleWords(
    words: ImmutableList<ExampleWordUi>,
    onIntent: (GlyphDetailsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (words.isEmpty()) return
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Text(
            text = stringResource(R.string.feature_alphabet_ui_glyph_words_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(start = 4.dp, end = 4.dp, bottom = 8.dp)
                .semantics { heading() },
        )
        words.forEachIndexed { index, word ->
            WordRow(word, onPlay = { onIntent(GlyphDetailsIntent.PlayWord(word.georgian)) })
            if (index < words.lastIndex) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        }
    }
}

@Composable
private fun WordRow(word: ExampleWordUi, onPlay: () -> Unit) {
    val subtitle = stringResource(
        R.string.feature_alphabet_ui_glyph_word_subtitle,
        word.transliteration,
        word.translation.localized(),
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .padding(4.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .weight(1f)
                // The Georgian word is read by its transliteration: TalkBack has no Georgian voice by default
                .clearAndSetSemantics { contentDescription = subtitle },
        ) {
            Text(
                text = word.parts.highlighted(MaterialTheme.colorScheme.primary),
                // Georgian text in the interface: Noto Sans Georgian on the titleLarge scale
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = NotoSansGeorgian,
                    localeList = LocaleList("ka"),
                ),
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        PlayWordButton(word, onPlay)
    }
}

@Composable
private fun PlayWordButton(word: ExampleWordUi, onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    val container by animateColorAsState(if (word.isPlaying) colors.primary else Color.Transparent)
    val content by animateColorAsState(if (word.isPlaying) colors.onPrimary else colors.primary)
    val description = stringResource(R.string.feature_alphabet_ui_glyph_word_play, word.transliteration)
    val playing = stringResource(R.string.feature_alphabet_ui_glyph_word_playing)
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(containerColor = container, contentColor = content),
        modifier = Modifier
            .size(48.dp)
            .semantics {
                contentDescription = description
                if (word.isPlaying) stateDescription = playing
            },
    ) {
        Icon(GeAlphIcons.Sound, contentDescription = null, modifier = Modifier.size(22.dp))
    }
}

@Composable
private fun ImmutableList<WordPart>.highlighted(color: Color): AnnotatedString = remember(this, color) {
    buildAnnotatedString {
        forEach { part ->
            if (part.highlighted) {
                withStyle(SpanStyle(color = color)) { append(part.text) }
            } else {
                append(part.text)
            }
        }
    }
}

private fun previewWord(georgian: String, translit: String, ru: String, en: String, playing: Boolean = false) =
    ExampleWordUi(
        georgian = georgian,
        parts = georgian.map { WordPart(it.toString(), it == 'ა') }.toImmutableList(),
        transliteration = translit,
        translation = LocalizedText(ru, en),
        audio = null,
        isPlaying = playing,
    )

private val PreviewWords = persistentListOf(
    previewWord("არა", "ара", "нет", "no"),
    previewWord("მადლობა", "мадлоба", "спасибо", "thank you", playing = true),
    previewWord("აფთიაქი", "аптиаки", "аптека", "pharmacy"),
    previewWord("ხაჭაპური", "хачапури", "хачапури", "khachapuri"),
    previewWord("ბანკი", "банки", "банк", "bank"),
)

@Preview(name = "light", widthDp = 360)
@Composable
private fun ExampleWordsLightPreview() {
    GeAlphTheme(darkTheme = false) {
        Box(Modifier.background(MaterialTheme.colorScheme.background)) {
            ExampleWords(PreviewWords, onIntent = {})
        }
    }
}

@Preview(name = "dark", widthDp = 360)
@Composable
private fun ExampleWordsDarkPreview() {
    GeAlphTheme(darkTheme = true) {
        Box(Modifier.background(MaterialTheme.colorScheme.background)) {
            ExampleWords(PreviewWords, onIntent = {})
        }
    }
}
