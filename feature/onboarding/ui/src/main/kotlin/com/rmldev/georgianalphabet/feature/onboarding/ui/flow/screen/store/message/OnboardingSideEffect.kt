package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.message

internal sealed interface OnboardingSideEffect {
    data object Finished : OnboardingSideEffect
}
