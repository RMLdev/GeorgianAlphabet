package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.state

import com.rmldev.georgianalphabet.core.designsystem.font.FontCatalog
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

internal class AllFontsStateToUiMapper @Inject constructor() : (AllFontsState) -> AllFontsUiState {

    override fun invoke(state: AllFontsState): AllFontsUiState {
        val letter = state.letter
            ?: return AllFontsUiState(letterId = state.letterId, isNotFound = state.isLoaded)
        val openFonts = FontCatalog.unlocked(state.unlockedFontIds).toSet()
        val recognized = state.progress?.recognizedFontIds.orEmpty()
        val tiles = FontCatalog.all.map { font ->
            val isOpen = font in openFonts
            FontTileUi(font, isOpen, isRecognized = isOpen && font.id in recognized)
        }
        return AllFontsUiState(
            letterId = letter.id,
            symbol = letter.glyph.toString(),
            letterName = when (state.knownScript) {
                KnownScript.CYRILLIC -> letter.nameCyrillic
                KnownScript.LATIN -> letter.nameLatin
            },
            openCount = openFonts.size,
            totalCount = FontCatalog.all.size,
            groups = tiles.groupBy { it.font.difficulty }
                .toSortedMap()
                .map { (difficulty, fonts) ->
                    FontGroupUi(difficulty, fonts.count { it.isOpen }, fonts.toImmutableList())
                }
                .toImmutableList(),
        )
    }
}
