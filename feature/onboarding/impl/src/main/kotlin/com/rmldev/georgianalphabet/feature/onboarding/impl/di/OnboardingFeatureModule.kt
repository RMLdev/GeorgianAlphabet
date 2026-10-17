package com.rmldev.georgianalphabet.feature.onboarding.impl.di

import com.rmldev.georgianalphabet.feature.onboarding.api.usecase.TransliterateNameUseCase
import com.rmldev.georgianalphabet.feature.onboarding.impl.domain.NameTransliterator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface OnboardingFeatureModule {

    @Binds
    fun bindTransliterateName(impl: NameTransliterator): TransliterateNameUseCase
}
