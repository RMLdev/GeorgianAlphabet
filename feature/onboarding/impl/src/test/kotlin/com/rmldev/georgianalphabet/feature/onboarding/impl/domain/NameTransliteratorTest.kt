package com.rmldev.georgianalphabet.feature.onboarding.impl.domain

import com.rmldev.georgianalphabet.feature.onboarding.api.model.TransliteratedFragment
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NameTransliteratorTest {

    private val transliterator = NameTransliterator()

    private fun georgian(text: String) = transliterator(text).joinToString("") { it.georgian }

    @Test
    fun `cyrillic names`() {
        assertEquals("რომან", georgian("Роман"))
        assertEquals("მარია", georgian("Мария"))
        assertEquals("ოლგა", georgian("Ольга"))
        assertEquals("ნატალია", georgian("Наталья"))
        assertEquals("იულია", georgian("Юлия"))
        assertEquals("შჩერბაკოვ", georgian("Щербаков"))
        assertEquals("პიოტრ", georgian("Пётр"))
        assertEquals("დმიტრი", georgian("Дмитрий"))
        assertEquals("ანდრეი", georgian("Андрей"))
    }

    @Test
    fun `latin names with digraphs`() {
        assertEquals("ტამარ", georgian("Tamar"))
        assertEquals("შოტა", georgian("Shota"))
        assertEquals("ხატია", georgian("Khatia"))
        assertEquals("ძაღლი", georgian("dzaghli"))
        assertEquals("ცოტნე", georgian("Tsotne"))
    }

    @Test
    fun `fragments keep source text and case`() {
        assertEquals(
            listOf(
                TransliteratedFragment("Sh", "შ"),
                TransliteratedFragment("o", "ო"),
                TransliteratedFragment("t", "ტ"),
                TransliteratedFragment("a", "ა"),
            ),
            transliterator("Shota"),
        )
        assertEquals(TransliteratedFragment("Р", "რ"), transliterator("Роман").first())
    }

    @Test
    fun `mixed input keeps spaces and hyphens and skips unknown symbols`() {
        assertEquals("ანნა-მარია შოტა", georgian("Анна-Мария Shota!1"))
        assertEquals("ტამარ რომან", georgian("Tamar Роман"))
        assertEquals("", georgian("123?!"))
    }

    @Test
    fun `soft and hard signs are skipped`() {
        assertTrue(transliterator("ь").isEmpty())
        assertEquals("ობიომ", georgian("объём"))
    }

    @Test
    fun `empty string`() {
        assertTrue(transliterator("").isEmpty())
    }
}
