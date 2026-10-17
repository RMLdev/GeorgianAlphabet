package com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.message

internal sealed interface OnboardingSideEffect {
    data object Finished : OnboardingSideEffect
}
