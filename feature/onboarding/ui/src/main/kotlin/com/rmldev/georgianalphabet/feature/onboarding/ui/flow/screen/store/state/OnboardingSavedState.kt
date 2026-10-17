package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state

import androidx.lifecycle.SavedStateHandle
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript

// Сохраняем только ввод пользователя, остальное (разбор имени) Actor пересчитывает в Init
private const val KEY_STEP = "onboarding_step"
private const val KEY_KNOWN_SCRIPT = "onboarding_known_script"
private const val KEY_USER_NAME = "onboarding_user_name"

internal fun SavedStateHandle.restoreOnboardingState(): OnboardingState = OnboardingState(
    step = get<String>(KEY_STEP)
        ?.let { name -> OnboardingStep.entries.firstOrNull { it.name == name } }
        ?: OnboardingStep.WELCOME,
    knownScript = get<String>(KEY_KNOWN_SCRIPT)
        ?.let { name -> KnownScript.entries.firstOrNull { it.name == name } },
    userName = get<String>(KEY_USER_NAME).orEmpty(),
)

internal fun SavedStateHandle.saveOnboardingState(state: OnboardingState) {
    set(KEY_STEP, state.step.name)
    set(KEY_KNOWN_SCRIPT, state.knownScript?.name)
    set(KEY_USER_NAME, state.userName)
}
