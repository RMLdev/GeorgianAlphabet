package com.rmldev.georgianalphabet.feature.alphabet.impl.data

import org.junit.Test
import kotlin.test.assertTrue

class LetterDetailsDataTest {

    private val letters = LETTERS.associateBy { it.id }

    @Test
    fun `similarity groups reference existing letters`() {
        (SIMILAR_BY_SHAPE + SIMILAR_BY_SOUND).flatten().forEach { id ->
            assertTrue(id in letters, "unknown letter id=$id")
        }
    }

    @Test
    fun `similarity groups have at least two letters`() {
        (SIMILAR_BY_SHAPE + SIMILAR_BY_SOUND).forEach { group ->
            assertTrue(group.size >= 2, "group $group")
        }
    }

    @Test
    fun `content belongs to existing letters`() {
        LETTER_CONTENT.keys.forEach { id -> assertTrue(id in letters, "unknown letter id=$id") }
    }

    @Test
    fun `every example word contains its letter and has transliteration`() {
        LETTER_CONTENT.forEach { (id, content) ->
            val glyph = letters.getValue(id).glyph
            content.words.forEach { word ->
                assertTrue(glyph in word.georgian, "${word.georgian} has no $glyph")
                assertTrue(word.transliterationCyrillic.isNotBlank(), word.georgian)
                assertTrue(word.transliterationLatin.isNotBlank(), word.georgian)
                assertTrue(word.translation.ru.isNotBlank() && word.translation.en.isNotBlank(), word.georgian)
            }
        }
    }
}
