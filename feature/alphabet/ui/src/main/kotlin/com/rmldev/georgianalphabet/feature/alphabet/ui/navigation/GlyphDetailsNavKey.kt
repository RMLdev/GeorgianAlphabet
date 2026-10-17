package com.rmldev.georgianalphabet.feature.alphabet.ui.navigation

import com.rmldev.georgianalphabet.core.navigation.api.NavigationKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("GlyphDetails")
data class GlyphDetailsNavKey(
    val id: Int,
) : NavigationKey {
    override val tag: String
        get() = this::class.java.simpleName
}