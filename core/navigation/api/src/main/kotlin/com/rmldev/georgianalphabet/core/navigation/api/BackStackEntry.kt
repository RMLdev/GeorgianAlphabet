package com.rmldev.georgianalphabet.core.navigation.api

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class BackStackEntry<T : NavigationKey>(
    val key: T,
    val id: String,
) {
    companion object {
        fun <T : NavigationKey> new(key: T) = BackStackEntry(key, UUID.randomUUID().toString())
    }
}