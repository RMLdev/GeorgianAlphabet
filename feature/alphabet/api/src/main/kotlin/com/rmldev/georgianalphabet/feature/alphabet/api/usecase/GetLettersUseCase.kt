package com.rmldev.georgianalphabet.feature.alphabet.api.usecase

import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter

interface GetLettersUseCase {
    /** Все 38 букв, отсортированные по [Letter.order]. */
    suspend operator fun invoke(): List<Letter>
}
