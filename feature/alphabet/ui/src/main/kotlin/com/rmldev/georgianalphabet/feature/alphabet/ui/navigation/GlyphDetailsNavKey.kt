package com.rmldev.georgianalphabet.feature.alphabet.ui.navigation

import com.rmldev.georgianalphabet.core.navigation.api.NavigationKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Карточка буквы.
 *
 * @param fontId шрифт, с которого открыть сцену (из «Все шрифты»); null — первый открытый.
 */
@Serializable
@SerialName("GlyphDetails")
data class GlyphDetailsNavKey(val id: Int, val fontId: String? = null) : NavigationKey {
    override val tag: String
        get() = this::class.java.simpleName
}
