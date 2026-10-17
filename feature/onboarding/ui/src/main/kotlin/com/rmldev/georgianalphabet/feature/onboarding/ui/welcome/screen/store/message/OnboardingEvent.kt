package com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.message

internal sealed interface OnboardingEvent {
    data object CompletionStarted : OnboardingEvent
}
