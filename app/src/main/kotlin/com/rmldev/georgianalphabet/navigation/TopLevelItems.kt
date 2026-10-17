package com.rmldev.georgianalphabet.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.rmldev.georgianalphabet.core.designsystem.icon.GeAlphIcons
import com.rmldev.georgianalphabet.feature.alphabet.api.AlphabetNavKey
import com.rmldev.georgianalphabet.feature.alphabet.ui.R as AlphabetR

data class TopLevelNavItem(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
)

val ALPHABET = TopLevelNavItem(
    GeAlphIcons.Alphabet,
    GeAlphIcons.Alphabet,
    AlphabetR.string.feature_alphabet_ui_screen_name,
    AlphabetR.string.feature_alphabet_ui_screen_name,
)

val TOP_LEVEL_ITEMS = mapOf(
    AlphabetNavKey to ALPHABET,
)