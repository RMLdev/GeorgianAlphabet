package com.rmldev.georgianalphabet.feature.alphabet.impl.domain

import com.rmldev.georgianalphabet.feature.alphabet.impl.data.StaticAlphabetRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GetLetterDetailsUseCaseImplTest {

    private val useCase = GetLetterDetailsUseCaseImpl(StaticAlphabetRepository())

    @Test
    fun `details of ani match the mockup`() = runTest {
        val details = assertNotNull(useCase(0))

        assertEquals(0, details.letterId)
        assertNotNull(details.association)
        assertEquals(listOf("არა", "მადლობა", "აფთიაქი", "ხაჭაპური", "ბანკი"), details.words.map { it.georgian })
        assertEquals("ara", details.words.first().transliterationLatin)
        assertEquals("нет", details.words.first().translation.ru)
        // ს и ჰ, по алфавиту
        assertEquals(listOf(17, 32), details.similarByShape)
        assertTrue(details.similarBySound.isEmpty())
    }

    @Test
    fun `similar letters come from every group of the letter`() = runTest {
        // ღ похожа на დ и ლ по виду и на გ по звуку
        val details = assertNotNull(useCase(22))

        assertEquals(listOf(3, 10), details.similarByShape)
        assertEquals(listOf(2), details.similarBySound)
    }

    @Test
    fun `letter without content has empty details`() = runTest {
        val details = assertNotNull(useCase(36))

        assertNull(details.association)
        assertTrue(details.words.isEmpty())
        assertTrue(details.similarByShape.isEmpty())
        assertTrue(details.similarBySound.isEmpty())
    }

    @Test
    fun `returns null for unknown id`() = runTest {
        assertNull(useCase(-1))
        assertNull(useCase(38))
    }
}
