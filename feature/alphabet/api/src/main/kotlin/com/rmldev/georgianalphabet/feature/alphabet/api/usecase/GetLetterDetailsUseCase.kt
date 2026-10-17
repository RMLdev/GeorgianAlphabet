package com.rmldev.georgianalphabet.feature.alphabet.api.usecase

import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LetterDetails

interface GetLetterDetailsUseCase {
    /** Детали буквы по [Letter.id] или null, если такой буквы нет. */
    suspend operator fun invoke(id: Int): LetterDetails?
}
