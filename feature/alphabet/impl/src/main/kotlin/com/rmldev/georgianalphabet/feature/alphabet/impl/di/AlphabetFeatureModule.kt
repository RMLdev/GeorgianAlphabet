package com.rmldev.georgianalphabet.feature.alphabet.impl.di

import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.GetLetterUseCase
import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.GetLettersUseCase
import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.ObserveLettersProgressUseCase
import com.rmldev.georgianalphabet.feature.alphabet.impl.data.AlphabetRepository
import com.rmldev.georgianalphabet.feature.alphabet.impl.data.StaticAlphabetRepository
import com.rmldev.georgianalphabet.feature.alphabet.impl.domain.GetLetterUseCaseImpl
import com.rmldev.georgianalphabet.feature.alphabet.impl.domain.GetLettersUseCaseImpl
import com.rmldev.georgianalphabet.feature.alphabet.impl.progress.ObserveLettersProgressUseCaseImpl
import com.rmldev.georgianalphabet.feature.alphabet.impl.progress.ProgressRepository
import com.rmldev.georgianalphabet.feature.alphabet.impl.progress.RoomProgressRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface AlphabetFeatureModule {

    @Binds
    fun bindRepository(impl: StaticAlphabetRepository): AlphabetRepository

    @Binds
    fun bindGetLetters(impl: GetLettersUseCaseImpl): GetLettersUseCase

    @Binds
    fun bindGetLetter(impl: GetLetterUseCaseImpl): GetLetterUseCase

    @Binds
    fun bindProgressRepository(impl: RoomProgressRepository): ProgressRepository

    @Binds
    fun bindObserveLettersProgress(impl: ObserveLettersProgressUseCaseImpl): ObserveLettersProgressUseCase
}
