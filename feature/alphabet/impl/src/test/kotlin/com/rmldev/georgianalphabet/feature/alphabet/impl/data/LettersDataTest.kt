package com.rmldev.georgianalphabet.feature.alphabet.impl.data

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LettersDataTest {

    private val legacyOrder = "აბგდევზთიკლმნოპჟრსტუფქღყშჩცძწჭხჯჰ"

    @Test
    fun `contains 38 letters with unique ids and orders`() {
        assertEquals(38, LETTERS.size)
        assertEquals(38, LETTERS.map { it.id }.toSet().size)
        assertEquals((1..38).toList(), LETTERS.map { it.order }.sorted())
        assertEquals(38, LETTERS.map { it.glyph }.toSet().size)
    }

    @Test
    fun `modern letters have lesson in 1 to 6`() {
        val modern = LETTERS.filterNot { it.archaic }
        assertEquals(33, modern.size)
        assertTrue(modern.all { it.lesson in 1..6 })
    }

    @Test
    fun `archaic letters have no lesson`() {
        val archaic = LETTERS.filter { it.archaic }
        assertEquals(5, archaic.size)
        assertTrue(archaic.all { it.lesson == null })
        assertEquals(setOf(33, 34, 35, 36, 37), archaic.map { it.id }.toSet())
    }

    @Test
    fun `ids 0 to 32 match legacy glyph order`() {
        legacyOrder.forEachIndexed { index, glyph ->
            val letter = LETTERS.single { it.id == index }
            assertEquals(glyph, letter.glyph, "id=$index")
            assertEquals(false, letter.archaic)
        }
    }

    @Test
    fun `archaic letters are placed in historical order`() {
        fun orderOf(c: Char) = LETTERS.single { it.glyph == c }.order
        assertEquals(orderOf('ზ') + 1, orderOf('ჱ'))
        assertEquals(orderOf('ნ') + 1, orderOf('ჲ'))
        assertEquals(orderOf('ტ') + 1, orderOf('ჳ'))
        assertEquals(orderOf('ხ') + 1, orderOf('ჴ'))
        assertEquals(orderOf('ჰ') + 1, orderOf('ჵ'))
    }

    @Test
    fun `no blank text fields`() {
        assertTrue(
            LETTERS.all {
                it.nameGeorgian.isNotBlank() && it.nameCyrillic.isNotBlank() &&
                    it.nameLatin.isNotBlank() && it.soundCyrillic.isNotBlank() &&
                    it.soundLatin.isNotBlank() && it.ipa.isNotBlank()
            },
        )
    }

    @Test
    fun `repository returns sorted letters and finds by id`() {
        val repo = StaticAlphabetRepository()
        assertEquals((1..38).toList(), repo.letters().map { it.order })
        assertEquals('ა', repo.letter(0)?.glyph)
        assertNull(repo.letter(99))
    }
}
