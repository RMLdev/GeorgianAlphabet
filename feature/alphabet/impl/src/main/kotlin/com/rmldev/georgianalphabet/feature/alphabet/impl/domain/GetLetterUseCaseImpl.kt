package com.rmldev.georgianalphabet.feature.alphabet.impl.domain

import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter
import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.GetLetterUseCase
import com.rmldev.georgianalphabet.feature.alphabet.impl.data.AlphabetRepository
import javax.inject.Inject

internal class GetLetterUseCaseImpl @Inject constructor(private val repository: AlphabetRepository) :
    GetLetterUseCase {
    override suspend fun invoke(id: Int): Letter? = repository.letter(id)
}
