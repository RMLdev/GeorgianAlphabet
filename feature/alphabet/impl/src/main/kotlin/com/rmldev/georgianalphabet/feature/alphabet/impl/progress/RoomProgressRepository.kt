package com.rmldev.georgianalphabet.feature.alphabet.impl.progress

import com.rmldev.georgianalphabet.feature.alphabet.api.model.LetterProgress
import com.rmldev.georgianalphabet.feature.alphabet.db.AlphabetFeatureDatabase
import com.rmldev.georgianalphabet.feature.alphabet.db.LetterProgressEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class RoomProgressRepository @Inject constructor(
    private val database: AlphabetFeatureDatabase,
    private val levelCalculator: LearningLevelCalculator,
) : ProgressRepository {

    override fun observeProgress(): Flow<Map<Int, LetterProgress>> = database.getLetterProgressDao()
        .observeAll()
        .map { entities -> entities.associate { it.letterId to it.toDomain() } }

    private fun LetterProgressEntity.toDomain() = LetterProgress(
        letterId = letterId,
        correct = correct,
        total = total,
        recognizedFontIds = recognizedFontIds,
        level = levelCalculator.level(correct, total, recognizedFontIds),
    )
}
