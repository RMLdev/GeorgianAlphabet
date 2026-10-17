package com.rmldev.georgianalphabet.feature.alphabet.api

import com.rmldev.georgianalphabet.core.navigation.api.TabKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("AlphabetNavKey")
object AlphabetNavKey : TabKey {
    override val tag: String
        get() = this::class.java.simpleName
}