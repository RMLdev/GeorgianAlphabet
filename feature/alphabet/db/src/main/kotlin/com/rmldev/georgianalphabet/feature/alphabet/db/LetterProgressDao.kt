package com.rmldev.georgianalphabet.feature.alphabet.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface LetterProgressDao {

    @Query("SELECT * FROM letter_progress")
    fun observeAll(): Flow<List<LetterProgressEntity>>

    @Query("SELECT * FROM letter_progress WHERE letter_id = :letterId")
    suspend fun get(letterId: Int): LetterProgressEntity?

    @Upsert
    suspend fun upsert(entity: LetterProgressEntity)
}
