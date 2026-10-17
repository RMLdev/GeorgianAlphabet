package com.rmldev.georgianalphabet.feature.alphabet.impl.domain

import com.rmldev.georgianalphabet.feature.alphabet.api.model.LetterDetails
import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.GetLetterDetailsUseCase
import com.rmldev.georgianalphabet.feature.alphabet.impl.data.AlphabetRepository
import javax.inject.Inject

internal class GetLetterDetailsUseCaseImpl @Inject constructor(private val repository: AlphabetRepository) :
    GetLetterDetailsUseCase {
    override suspend fun invoke(id: Int): LetterDetails? = repository.letterDetails(id)
}
