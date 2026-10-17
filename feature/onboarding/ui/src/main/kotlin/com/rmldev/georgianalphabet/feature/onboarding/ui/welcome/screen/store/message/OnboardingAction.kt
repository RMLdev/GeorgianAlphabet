package com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.message

internal sealed interface OnboardingAction {
    data object Complete : OnboardingAction
}
