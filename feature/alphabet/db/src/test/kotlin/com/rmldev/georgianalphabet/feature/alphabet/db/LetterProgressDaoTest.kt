package com.rmldev.georgianalphabet.feature.alphabet.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@Database(entities = [LetterProgressEntity::class], version = 1, exportSchema = false)
internal abstract class TestAlphabetDatabase :
    RoomDatabase(),
    AlphabetFeatureDatabase

class LetterProgressDaoTest {

    private val db = Room.inMemoryDatabaseBuilder<TestAlphabetDatabase>()
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()

    private val dao = db.getLetterProgressDao()

    @After
    fun tearDown() = db.close()

    @Test
    fun `empty on clean install`() = runTest {
        dao.observeAll().test {
            assertEquals(emptyList(), awaitItem())
        }
        assertNull(dao.get(0))
    }

    @Test
    fun `upsert inserts and replaces, font ids round-trip`() = runTest {
        dao.observeAll().test {
            assertEquals(emptyList(), awaitItem())

            val first = LetterProgressEntity(letterId = 3, correct = 1, total = 2, recognizedFontIds = setOf("noto"))
            dao.upsert(first)
            assertEquals(listOf(first), awaitItem())

            val updated = first.copy(correct = 2, total = 3, recognizedFontIds = setOf("noto", "bpg_glaho"))
            dao.upsert(updated)
            assertEquals(listOf(updated), awaitItem())
        }
        assertEquals(setOf("noto", "bpg_glaho"), dao.get(3)?.recognizedFontIds)
    }
}
