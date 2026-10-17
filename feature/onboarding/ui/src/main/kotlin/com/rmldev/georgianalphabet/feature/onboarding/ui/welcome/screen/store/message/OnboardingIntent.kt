package com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.message

internal sealed interface OnboardingIntent {
    data object StartClicked : OnboardingIntent

    data object SkipClicked : OnboardingIntent
}
