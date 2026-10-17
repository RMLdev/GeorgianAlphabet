package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.message

import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript

internal sealed interface OnboardingIntent {
    /** Стрелка «Назад» в шапке шагов 2–3 и системный «Назад». */
    data object BackClicked : OnboardingIntent

    // Приветствие
    data object StartClicked : OnboardingIntent

    data object SkipClicked : OnboardingIntent

    // Знакомый алфавит
    data class ScriptSelected(val script: KnownScript) : OnboardingIntent

    data object NextClicked : OnboardingIntent

    // Имя
    data class NameChanged(val name: String) : OnboardingIntent

    /** «Пропустить» в шапке шага имени: алфавит сохраняется, имя нет. */
    data object SkipNameClicked : OnboardingIntent

    /** «Начать учить» и IME action Done. */
    data object FinishClicked : OnboardingIntent
}
