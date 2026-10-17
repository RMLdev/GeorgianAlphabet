package com.rmldev.georgianalphabet.feature.preferences.api.usecase

import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript

interface SetKnownScriptUseCase {
    suspend operator fun invoke(knownScript: KnownScript)
}
