package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state

import com.rmldev.georgianalphabet.feature.onboarding.api.model.TransliteratedFragment
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript

/** Шаги онбординга по порядку: приветствие (задача 8), алфавит (9), имя (10). */
internal enum class OnboardingStep {
    WELCOME,
    SCRIPT,
    NAME,
    ;

    val previous: OnboardingStep? get() = entries.getOrNull(ordinal - 1)

    val next: OnboardingStep? get() = entries.getOrNull(ordinal + 1)
}

internal data class OnboardingState(
    val step: OnboardingStep = OnboardingStep.WELCOME,
    /** `null`, пока не прочитано значение по локали. */
    val knownScript: KnownScript? = null,
    /** Имя как введено, без обрезки: обрезает CompleteOnboardingUseCase. */
    val userName: String = "",
    val nameFragments: List<TransliteratedFragment> = emptyList(),
    val completing: Boolean = false,
)
