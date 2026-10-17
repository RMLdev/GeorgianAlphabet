package com.rmldev.georgianalphabet.feature.alphabet.api.model

import kotlinx.serialization.Serializable

@Serializable
data class Letter(
    val id: Int,
    val glyph: Char,
    val nameGeorgian: String,
    val nameCyrillic: String,
    val nameLatin: String,
    val soundCyrillic: String,
    val soundLatin: String,
    val ipa: String,
    val lesson: Int?,
    val archaic: Boolean,
    val order: Int,
)
