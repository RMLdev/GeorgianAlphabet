package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state

import com.rmldev.georgianalphabet.core.audio.api.AudioAssets
import com.rmldev.georgianalphabet.core.designsystem.font.FontCatalog
import com.rmldev.georgianalphabet.core.designsystem.font.GeorgianFont
import com.rmldev.georgianalphabet.feature.alphabet.api.model.ExampleWord
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LearningLevel
import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LetterProgress
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject
import kotlin.math.roundToInt

private const val PERCENT = 100

internal class GlyphDetailsStateToUiMapper @Inject constructor() : (GlyphDetailsState) -> GlyphDetailsUiState {

    override fun invoke(state: GlyphDetailsState): GlyphDetailsUiState {
        val letter = state.letter
            ?: return GlyphDetailsUiState(letterId = state.glyphId, isNotFound = state.isLoaded)
        val details = state.details
        val openFonts = FontCatalog.unlocked(state.unlockedFontIds)
        return GlyphDetailsUiState(
            letterId = letter.id,
            symbol = letter.glyph.toString(),
            stage = GlyphStageUi(
                fonts = openFonts.toImmutableList(),
                currentFontIndex = openFonts.indexOfFirst { it.id == state.currentFontId }.coerceAtLeast(0),
                lockedFontsCount = FontCatalog.all.size - openFonts.size,
            ),
            header = letter.toHeader(state),
            mastery = state.progress.toMastery(openFonts),
            association = details?.association,
            words = details?.words.orEmpty().map { it.toUi(letter.glyph, state) }.toImmutableList(),
            similar = SimilarLettersUi(
                byShape = details?.similarByShape.orEmpty().toSimilar(state),
                bySound = details?.similarBySound.orEmpty().toSimilar(state),
            ),
        )
    }

    private fun Letter.toHeader(state: GlyphDetailsState): LetterHeaderUi {
        val nameAudio = AudioAssets.letter(id)
        return LetterHeaderUi(
            nameGeorgian = nameGeorgian,
            name = name(state.knownScript),
            sound = when (state.knownScript) {
                KnownScript.CYRILLIC -> soundCyrillic
                KnownScript.LATIN -> soundLatin
            },
            knownScript = state.knownScript,
            ipa = ipa,
            archaic = archaic,
            nameAudio = nameAudio,
            isNamePlaying = state.playingId == nameAudio,
        )
    }

    // Font ids outside the catalog (removed fonts) are not counted
    private fun LetterProgress?.toMastery(openFonts: List<GeorgianFont>): MasteryUi {
        val recognized = this?.recognizedFontIds.orEmpty()
        return MasteryUi(
            level = this?.level ?: LearningLevel.NEW,
            recognizedCount = FontCatalog.all.count { it.id in recognized },
            fontsCount = FontCatalog.all.size,
            accuracyPercent = this?.takeIf { it.total > 0 }?.run { (correct * PERCENT.toFloat() / total).roundToInt() },
            cells = FontCatalog.all.map { font ->
                val status = when {
                    font.id in recognized -> FontCellStatus.RECOGNIZED
                    font in openFonts -> FontCellStatus.LEARNING
                    else -> FontCellStatus.LOCKED
                }
                FontCellUi(font, status)
            }.toImmutableList(),
        )
    }

    private fun ExampleWord.toUi(letter: Char, state: GlyphDetailsState) = ExampleWordUi(
        georgian = georgian,
        parts = georgian.splitByLetter(letter).toImmutableList(),
        transliteration = when (state.knownScript) {
            KnownScript.CYRILLIC -> transliterationCyrillic
            KnownScript.LATIN -> transliterationLatin
        },
        translation = translation,
        audio = audioAsset,
        isPlaying = audioAsset != null && audioAsset == state.playingId,
    )

    private fun List<Int>.toSimilar(state: GlyphDetailsState) = mapNotNull { id ->
        state.relatedLetters[id]?.let { SimilarLetterUi(it.id, it.glyph.toString(), it.name(state.knownScript)) }
    }.toImmutableList()

    private fun Letter.name(script: KnownScript) = when (script) {
        KnownScript.CYRILLIC -> nameCyrillic
        KnownScript.LATIN -> nameLatin
    }
}

/** «არბუზი» по «ა» → [«ა»*, «რბუზი»]: соседние символы с одинаковой подсветкой склеиваются. */
internal fun String.splitByLetter(letter: Char): List<WordPart> {
    val parts = mutableListOf<WordPart>()
    for (char in this) {
        val highlighted = char == letter
        val last = parts.lastOrNull()
        if (last != null && last.highlighted == highlighted) {
            parts[parts.lastIndex] = last.copy(text = last.text + char)
        } else {
            parts += WordPart(char.toString(), highlighted)
        }
    }
    return parts
}
