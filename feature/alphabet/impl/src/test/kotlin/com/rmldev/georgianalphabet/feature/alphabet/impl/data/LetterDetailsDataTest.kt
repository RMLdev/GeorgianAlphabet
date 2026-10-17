package com.rmldev.georgianalphabet.feature.alphabet.impl.data

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class LetterDetailsDataTest {

    private val letters = LETTERS.associateBy { it.id }
    private val modern = LETTERS.filterNot { it.archaic }
    private val repository = StaticAlphabetRepository()

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
    fun `every modern letter has association and 3 to 5 words`() {
        assertEquals(33, modern.size)
        modern.forEach { letter ->
            val content = assertNotNull(LETTER_CONTENT[letter.id], "no content for ${letter.glyph}")
            val association = assertNotNull(content.association, "no association for ${letter.glyph}")
            assertTrue(association.ru.isNotBlank() && association.en.isNotBlank(), "${letter.glyph}")
            assertTrue(content.words.size in 3..5, "${letter.glyph} has ${content.words.size} words")
            assertEquals(content.words.size, content.words.distinctBy { it.georgian }.size, "${letter.glyph}")
        }
    }

    @Test
    fun `archaic letters have no words`() {
        LETTERS.filter { it.archaic }.forEach { letter ->
            assertTrue(LETTER_CONTENT[letter.id]?.words.isNullOrEmpty(), "${letter.glyph}")
        }
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

    @Test
    fun `transliteration follows letter by letter rules`() {
        LETTER_CONTENT.values.flatMap { it.words }.forEach { word ->
            assertEquals(latin(word.georgian), word.transliterationLatin, word.georgian)
            assertEquals(cyrillic(word.georgian), word.transliterationCyrillic, word.georgian)
        }
    }

    @Test
    fun `similarity is symmetric`() {
        modern.forEach { letter ->
            val details = assertNotNull(repository.letterDetails(letter.id))
            details.similarBySound.forEach { other ->
                assertTrue(letter.id in repository.letterDetails(other)!!.similarBySound, "${letter.glyph}–$other")
            }
            details.similarByShape.forEach { other ->
                assertTrue(letter.id in repository.letterDetails(other)!!.similarByShape, "${letter.glyph}–$other")
            }
            assertTrue(letter.id !in details.similarBySound + details.similarByShape, "${letter.glyph}")
        }
    }

    @Test
    fun `sound pairs from the task are present`() {
        val pairs = listOf("ქკ", "თტ", "ფპ", "ცწ", "ჩჭ", "ღგ", "ხყ")
        val byGlyph = LETTERS.associateBy { it.glyph }
        pairs.forEach { pair ->
            val (a, b) = pair.map { byGlyph.getValue(it).id }
            assertTrue(b in repository.letterDetails(a)!!.similarBySound, pair)
        }
    }

    private fun latin(georgian: String) = georgian.map { LATIN.getValue(it) }.joinToString("")

    private fun cyrillic(georgian: String) = georgian.mapIndexed { index, char ->
        if (char == 'ე' && index == 0) "э" else CYRILLIC.getValue(char)
    }.joinToString("")

    private companion object {
        val LATIN = mapOf(
            'ა' to "a", 'ბ' to "b", 'გ' to "g", 'დ' to "d", 'ე' to "e", 'ვ' to "v", 'ზ' to "z",
            'თ' to "t", 'ი' to "i", 'კ' to "k'", 'ლ' to "l", 'მ' to "m", 'ნ' to "n", 'ო' to "o",
            'პ' to "p'", 'ჟ' to "zh", 'რ' to "r", 'ს' to "s", 'ტ' to "t'", 'უ' to "u", 'ფ' to "p",
            'ქ' to "k", 'ღ' to "gh", 'ყ' to "q'", 'შ' to "sh", 'ჩ' to "ch", 'ც' to "ts", 'ძ' to "dz",
            'წ' to "ts'", 'ჭ' to "ch'", 'ხ' to "kh", 'ჯ' to "j", 'ჰ' to "h",
        )
        val CYRILLIC = mapOf(
            'ა' to "а", 'ბ' to "б", 'გ' to "г", 'დ' to "д", 'ე' to "е", 'ვ' to "в", 'ზ' to "з",
            'თ' to "т", 'ი' to "и", 'კ' to "кʼ", 'ლ' to "л", 'მ' to "м", 'ნ' to "н", 'ო' to "о",
            'პ' to "пʼ", 'ჟ' to "ж", 'რ' to "р", 'ს' to "с", 'ტ' to "тʼ", 'უ' to "у", 'ფ' to "п",
            'ქ' to "к", 'ღ' to "гх", 'ყ' to "къ", 'შ' to "ш", 'ჩ' to "ч", 'ც' to "ц", 'ძ' to "дз",
            'წ' to "цʼ", 'ჭ' to "чʼ", 'ხ' to "х", 'ჯ' to "дж", 'ჰ' to "х",
        )
    }
}
