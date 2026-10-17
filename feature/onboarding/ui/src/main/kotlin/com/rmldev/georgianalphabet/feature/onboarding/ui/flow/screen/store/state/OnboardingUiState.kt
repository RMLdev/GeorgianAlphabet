package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state

import androidx.compose.runtime.Stable

@Stable
internal data class OnboardingUiState(
    val step: OnboardingStep,
    /** Кнопки неактивны, пока сохраняется результат онбординга. */
    val buttonsEnabled: Boolean,
    val welcome: WelcomeStepUiState,
    val script: ScriptStepUiState,
    val name: NameStepUiState,
)
