package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state

import androidx.compose.runtime.Stable

/** Шаг «Приветствие» (задача 8). */
@Stable
internal data class WelcomeStepUiState(val buttonsEnabled: Boolean)

internal fun OnboardingState.toWelcomeStepUi(): WelcomeStepUiState = WelcomeStepUiState(
    buttonsEnabled = !completing,
)
