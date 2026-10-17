package com.rmldev.georgianalphabet.feature.alphabet.impl.progress

import com.rmldev.georgianalphabet.feature.alphabet.api.model.LetterProgress
import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.ObserveLettersProgressUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ObserveLettersProgressUseCaseImpl @Inject constructor(private val repository: ProgressRepository) :
    ObserveLettersProgressUseCase {
    override fun invoke(): Flow<Map<Int, LetterProgress>> = repository.observeProgress()
}
