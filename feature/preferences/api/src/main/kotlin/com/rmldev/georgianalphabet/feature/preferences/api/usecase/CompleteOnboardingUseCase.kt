package com.rmldev.georgianalphabet.feature.preferences.api.usecase

import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript

interface CompleteOnboardingUseCase {
    /**
     * Атомарно сохраняет выбор из онбординга и ставит `onboardingCompleted`.
     * [userName] обрезается, пустое имя сохраняется как `null`.
     * `null` в [knownScript] оставляет значение по локали.
     */
    suspend operator fun invoke(knownScript: KnownScript?, userName: String?)
}
