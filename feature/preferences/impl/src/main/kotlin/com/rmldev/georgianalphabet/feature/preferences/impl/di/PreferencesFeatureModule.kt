package com.rmldev.georgianalphabet.feature.preferences.impl.di

import com.rmldev.georgianalphabet.feature.preferences.api.usecase.CompleteOnboardingUseCase
import com.rmldev.georgianalphabet.feature.preferences.api.usecase.ObserveUserPreferencesUseCase
import com.rmldev.georgianalphabet.feature.preferences.api.usecase.SetKnownScriptUseCase
import com.rmldev.georgianalphabet.feature.preferences.api.usecase.SetUserNameUseCase
import com.rmldev.georgianalphabet.feature.preferences.impl.data.DataStoreUserPreferencesRepository
import com.rmldev.georgianalphabet.feature.preferences.impl.data.UserPreferencesRepository
import com.rmldev.georgianalphabet.feature.preferences.impl.domain.CompleteOnboardingUseCaseImpl
import com.rmldev.georgianalphabet.feature.preferences.impl.domain.ObserveUserPreferencesUseCaseImpl
import com.rmldev.georgianalphabet.feature.preferences.impl.domain.SetKnownScriptUseCaseImpl
import com.rmldev.georgianalphabet.feature.preferences.impl.domain.SetUserNameUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface PreferencesFeatureModule {

    @Binds
    fun bindRepository(impl: DataStoreUserPreferencesRepository): UserPreferencesRepository

    @Binds
    fun bindObserve(impl: ObserveUserPreferencesUseCaseImpl): ObserveUserPreferencesUseCase

    @Binds
    fun bindCompleteOnboarding(impl: CompleteOnboardingUseCaseImpl): CompleteOnboardingUseCase

    @Binds
    fun bindSetKnownScript(impl: SetKnownScriptUseCaseImpl): SetKnownScriptUseCase

    @Binds
    fun bindSetUserName(impl: SetUserNameUseCaseImpl): SetUserNameUseCase
}
