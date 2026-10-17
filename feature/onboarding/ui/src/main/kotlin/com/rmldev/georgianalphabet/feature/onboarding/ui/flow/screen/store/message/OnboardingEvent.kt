package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.message

import com.rmldev.georgianalphabet.feature.onboarding.api.model.TransliteratedFragment
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.OnboardingStep
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript

internal sealed interface OnboardingEvent {
    data class StepChanged(val step: OnboardingStep) : OnboardingEvent

    data class ScriptSelected(val script: KnownScript) : OnboardingEvent

    data class NameChanged(val name: String, val fragments: List<TransliteratedFragment>) : OnboardingEvent

    data object CompletionStarted : OnboardingEvent
}
