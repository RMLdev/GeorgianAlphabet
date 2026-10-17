package com.rmldev.georgianalphabet.core.navigation.api

import kotlinx.serialization.Serializable

@Serializable
sealed interface ScreenOverlay {

    @Serializable
    data object Hidden : ScreenOverlay

    @Serializable
    data object Blocking : ScreenOverlay

    @Serializable
    data object Modal : ScreenOverlay
}