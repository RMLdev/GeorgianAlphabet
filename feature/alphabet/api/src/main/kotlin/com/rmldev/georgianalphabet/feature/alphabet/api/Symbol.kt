package com.rmldev.georgianalphabet.feature.alphabet.api

import kotlinx.serialization.Serializable

@Serializable
data class Symbol(
    val id: Int,
    val char: String,
)