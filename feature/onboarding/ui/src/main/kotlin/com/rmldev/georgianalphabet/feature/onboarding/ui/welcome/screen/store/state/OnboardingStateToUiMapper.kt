package com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.state

import javax.inject.Inject

internal class OnboardingStateToUiMapper @Inject constructor() : (OnboardingState) -> OnboardingUiState {

    override fun invoke(state: OnboardingState): OnboardingUiState = OnboardingUiState(
        buttonsEnabled = !state.completing,
    )
}
