package com.rmldev.georgianalphabet.feature.alphabet.ui.navigation

import com.rmldev.georgianalphabet.core.navigation.api.NavigationKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Буква [letterId] во всех шрифтах коллекции, открывается из карточки буквы. */
@Serializable
@SerialName("AllFonts")
data class AllFontsNavKey(val letterId: Int) : NavigationKey {
    override val tag: String
        get() = this::class.java.simpleName
}
