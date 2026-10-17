package com.rmldev.georgianalphabet.feature.alphabet.ui.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.rmldev.georgianalphabet.core.navigation.api.NavigationKey
import com.rmldev.georgianalphabet.core.navigation.api.Navigator
import com.rmldev.georgianalphabet.feature.alphabet.api.AlphabetNavKey
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.GlyphDetailsRoute
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.AlphabetGreedRoute

fun EntryProviderScope<NavigationKey>.provideEntries(navigator: Navigator) {
    entry<AlphabetNavKey> {
        AlphabetGreedRoute(
            onGlyphClick = { navigator.navigateTo(GlyphDetailsNavKey(it)) }
        )
    }

    entry<GlyphDetailsNavKey> { key ->
        GlyphDetailsRoute(glyphId = key.id)
    }
}