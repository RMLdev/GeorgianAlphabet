package com.rmldev.georgianalphabet.feature.alphabet.api.usecase

import com.rmldev.georgianalphabet.feature.alphabet.api.model.LetterProgress
import kotlinx.coroutines.flow.Flow

interface ObserveLettersProgressUseCase {
    /** Прогресс по каждой букве, ключ — [LetterProgress.letterId]. Букв без записи нет в словаре: они новые. */
    operator fun invoke(): Flow<Map<Int, LetterProgress>>
}
