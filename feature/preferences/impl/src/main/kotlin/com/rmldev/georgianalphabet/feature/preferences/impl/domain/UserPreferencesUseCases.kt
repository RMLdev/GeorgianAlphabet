package com.rmldev.georgianalphabet.feature.preferences.impl.domain

import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript
import com.rmldev.georgianalphabet.feature.preferences.api.model.UserPreferences
import com.rmldev.georgianalphabet.feature.preferences.api.usecase.CompleteOnboardingUseCase
import com.rmldev.georgianalphabet.feature.preferences.api.usecase.ObserveUserPreferencesUseCase
import com.rmldev.georgianalphabet.feature.preferences.api.usecase.SetKnownScriptUseCase
import com.rmldev.georgianalphabet.feature.preferences.api.usecase.SetUserNameUseCase
import com.rmldev.georgianalphabet.feature.preferences.impl.data.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ObserveUserPreferencesUseCaseImpl @Inject constructor(
    private val repository: UserPreferencesRepository,
) : ObserveUserPreferencesUseCase {
    override fun invoke(): Flow<UserPreferences> = repository.observe()
}

internal class CompleteOnboardingUseCaseImpl @Inject constructor(private val repository: UserPreferencesRepository) :
    CompleteOnboardingUseCase {
    override suspend fun invoke(knownScript: KnownScript?, userName: String?) =
        repository.completeOnboarding(knownScript, userName)
}

internal class SetKnownScriptUseCaseImpl @Inject constructor(private val repository: UserPreferencesRepository) :
    SetKnownScriptUseCase {
    override suspend fun invoke(knownScript: KnownScript) = repository.setKnownScript(knownScript)
}

internal class SetUserNameUseCaseImpl @Inject constructor(private val repository: UserPreferencesRepository) :
    SetUserNameUseCase {
    override suspend fun invoke(userName: String?) = repository.setUserName(userName)
}
