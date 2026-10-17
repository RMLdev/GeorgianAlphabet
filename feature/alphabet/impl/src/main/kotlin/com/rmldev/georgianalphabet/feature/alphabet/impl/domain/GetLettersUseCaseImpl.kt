package com.rmldev.georgianalphabet.feature.alphabet.impl.domain

import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter
import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.GetLettersUseCase
import com.rmldev.georgianalphabet.feature.alphabet.impl.data.AlphabetRepository
import javax.inject.Inject

internal class GetLettersUseCaseImpl @Inject constructor(private val repository: AlphabetRepository) :
    GetLettersUseCase {
    override suspend fun invoke(): List<Letter> = repository.letters()
}
