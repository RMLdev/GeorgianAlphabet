package com.rmldev.georgianalphabet.feature.alphabet.impl.progress

import com.rmldev.georgianalphabet.feature.alphabet.api.model.LetterProgress
import kotlinx.coroutines.flow.Flow

internal interface ProgressRepository {
    fun observeProgress(): Flow<Map<Int, LetterProgress>>
}
