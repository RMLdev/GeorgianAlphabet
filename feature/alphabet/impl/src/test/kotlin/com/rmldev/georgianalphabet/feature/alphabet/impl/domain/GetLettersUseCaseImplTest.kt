package com.rmldev.georgianalphabet.feature.alphabet.impl.domain

import com.rmldev.georgianalphabet.feature.alphabet.impl.data.StaticAlphabetRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class GetLettersUseCaseImplTest {

    @Test
    fun `returns 38 letters sorted by order`() = runTest {
        val result = GetLettersUseCaseImpl(StaticAlphabetRepository())()

        assertEquals(38, result.size)
        assertEquals((1..38).toList(), result.map { it.order })
    }
}
