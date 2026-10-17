package com.rmldev.georgianalphabet.core.navigation.api

interface NavigationKey {
    val tag: String
}

interface TabKey : NavigationKey

interface OverlayScreenKey : NavigationKey {
    val overlayMode: ScreenOverlay
}