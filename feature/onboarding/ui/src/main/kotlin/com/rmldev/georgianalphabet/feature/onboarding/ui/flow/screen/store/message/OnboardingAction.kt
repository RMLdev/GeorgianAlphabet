package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.message

import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript

internal sealed interface OnboardingAction {
    /** Предвыбор алфавита по локали и разбор имени после восстановления состояния. */
    data object Init : OnboardingAction

    data object GoForward : OnboardingAction

    data object GoBack : OnboardingAction

    data class SelectScript(val script: KnownScript) : OnboardingAction

    data class ChangeName(val name: String) : OnboardingAction

    /** «Пропустить» на приветствии: значения по умолчанию, без имени. */
    data object SkipAll : OnboardingAction

    /** Завершение с выбранным алфавитом; имя сохраняется, только если [saveName]. */
    data class Finish(val saveName: Boolean) : OnboardingAction
}
