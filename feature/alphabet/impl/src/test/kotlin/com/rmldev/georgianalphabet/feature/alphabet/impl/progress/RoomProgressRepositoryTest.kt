package com.rmldev.georgianalphabet.feature.alphabet.impl.progress

import com.rmldev.georgianalphabet.feature.alphabet.api.model.LearningLevel
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LetterProgress
import com.rmldev.georgianalphabet.feature.alphabet.db.AlphabetFeatureDatabase
import com.rmldev.georgianalphabet.feature.alphabet.db.LetterProgressDao
import com.rmldev.georgianalphabet.feature.alphabet.db.LetterProgressEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

private class FakeDao(initial: List<LetterProgressEntity>) : LetterProgressDao {
    val entities = MutableStateFlow(initial)
    override fun observeAll(): Flow<List<LetterProgressEntity>> = entities
    override suspend fun get(letterId: Int) = entities.value.firstOrNull { it.letterId == letterId }
    override suspend fun upsert(entity: LetterProgressEntity) {
        entities.value = entities.value.filterNot { it.letterId == entity.letterId } + entity
    }
}

class RoomProgressRepositoryTest {

    private fun repository(vararg entities: LetterProgressEntity): ProgressRepository {
        val dao = FakeDao(entities.toList())
        val db = object : AlphabetFeatureDatabase {
            override fun getLetterProgressDao(): LetterProgressDao = dao
        }
        return RoomProgressRepository(db, LearningLevelCalculator())
    }

    @Test
    fun `clean install gives empty progress`() = runTest {
        assertEquals(emptyMap(), repository().observeProgress().first())
    }

    @Test
    fun `maps entities with level`() = runTest {
        val progress = repository(
            LetterProgressEntity(letterId = 0, correct = 0, total = 2, recognizedFontIds = emptySet()),
            LetterProgressEntity(letterId = 1, correct = 3, total = 4, recognizedFontIds = setOf("noto")),
        ).observeProgress().first()

        assertEquals(LearningLevel.NEW, progress.getValue(0).level)
        assertEquals(
            LetterProgress(
                letterId = 1,
                correct = 3,
                total = 4,
                recognizedFontIds = setOf("noto"),
                LearningLevel.FAMILIAR,
            ),
            progress.getValue(1),
        )
    }
}
