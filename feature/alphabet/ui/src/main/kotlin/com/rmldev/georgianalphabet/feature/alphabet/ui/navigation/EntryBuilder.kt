package com.rmldev.georgianalphabet.feature.alphabet.ui.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.rmldev.georgianalphabet.core.navigation.api.NavigationKey
import com.rmldev.georgianalphabet.core.navigation.api.Navigator
import com.rmldev.georgianalphabet.feature.alphabet.api.AlphabetNavKey
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.GlyphDetailsRoute
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.AllFontsRoute
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.AlphabetGridRoute

fun EntryProviderScope<NavigationKey>.provideEntries(navigator: Navigator) {
    entry<AlphabetNavKey> {
        AlphabetGridRoute(
            onGlyphClick = { navigator.navigateTo(GlyphDetailsNavKey(it)) },
        )
    }

    entry<GlyphDetailsNavKey> { key ->
        GlyphDetailsRoute(
            glyphId = key.id,
            fontId = key.fontId,
            onBack = navigator::back,
            // A similar letter replaces the card, so "Back" still leads to the alphabet
            onOpenLetter = { id ->
                navigator.popWhere { it == key }
                navigator.navigateTo(GlyphDetailsNavKey(id))
            },
            onOpenAllFonts = { navigator.navigateTo(AllFontsNavKey(it)) },
        )
    }

    entry<AllFontsNavKey> { key ->
        AllFontsRoute(
            letterId = key.letterId,
            onBack = navigator::back,
            // Back to the card in the chosen font: the old card is replaced, not duplicated
            onOpenFont = { letterId, fontId ->
                navigator.popWhere { it == key || it is GlyphDetailsNavKey && it.id == letterId }
                navigator.navigateTo(GlyphDetailsNavKey(letterId, fontId))
            },
        )
    }
}
