package com.rmldev.georgianalphabet.feature.onboarding.api

import com.rmldev.georgianalphabet.core.navigation.api.OverlayScreenKey
import com.rmldev.georgianalphabet.core.navigation.api.ScreenOverlay
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Онбординг поверх табов, без нижней навигации. «Назад» на нём закрывает приложение. */
@Serializable
@SerialName("OnboardingNavKey")
object OnboardingNavKey : OverlayScreenKey {
    override val tag: String
        get() = this::class.java.simpleName

    override val overlayMode: ScreenOverlay
        get() = ScreenOverlay.Blocking
}
