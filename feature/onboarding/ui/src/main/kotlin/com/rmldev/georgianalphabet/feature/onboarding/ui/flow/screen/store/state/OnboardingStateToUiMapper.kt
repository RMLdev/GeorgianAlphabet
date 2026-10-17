package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state

import javax.inject.Inject

internal class OnboardingStateToUiMapper @Inject constructor() : (OnboardingState) -> OnboardingUiState {

    override fun invoke(state: OnboardingState): OnboardingUiState = OnboardingUiState(
        step = state.step,
        buttonsEnabled = !state.completing,
        welcome = state.toWelcomeStepUi(),
        script = state.toScriptStepUi(),
        name = state.toNameStepUi(),
    )
}
