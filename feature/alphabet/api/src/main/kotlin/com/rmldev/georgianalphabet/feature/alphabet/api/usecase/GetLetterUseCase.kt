package com.rmldev.georgianalphabet.feature.alphabet.api.usecase

import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter

interface GetLetterUseCase {
    /** Буква по [Letter.id] или null, если такой нет. */
    suspend operator fun invoke(id: Int): Letter?
}
