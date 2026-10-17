package com.rmldev.georgianalphabet.feature.alphabet.impl.domain

import com.rmldev.georgianalphabet.feature.alphabet.impl.data.StaticAlphabetRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetLetterUseCaseImplTest {

    private val useCase = GetLetterUseCaseImpl(StaticAlphabetRepository())

    @Test
    fun `finds letter by id`() = runTest {
        assertEquals('ბ', useCase(1)?.glyph)
        assertEquals(1, useCase(1)?.id)
        assertEquals(true, useCase(37)?.archaic)
    }

    @Test
    fun `returns null for unknown id`() = runTest {
        assertNull(useCase(-1))
        assertNull(useCase(38))
    }
}
