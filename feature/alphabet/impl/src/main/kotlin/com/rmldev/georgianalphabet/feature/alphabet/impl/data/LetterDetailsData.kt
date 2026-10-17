package com.rmldev.georgianalphabet.feature.alphabet.impl.data

import com.rmldev.georgianalphabet.feature.alphabet.api.model.ExampleWord
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LocalizedText

/** Ассоциация и примеры слов одной буквы. */
internal data class LetterContent(
    val association: LocalizedText?,
    val words: List<ExampleWord>,
)

private fun word(georgian: String, cyrillic: String, latin: String, ru: String, en: String) =
    ExampleWord(georgian, cyrillic, latin, LocalizedText(ru, en))

/**
 * Группы похожих по начертанию букв (id из [LETTERS]): каждая буква группы похожа на остальные.
 * Черновик по concept.md 2.4 и макету карточки, сверить по шрифтам каталога.
 */
internal val SIMILAR_BY_SHAPE: List<Set<Int>> = listOf(
    setOf(0, 17, 32), // ა ს ჰ
    setOf(3, 22), // დ ღ
    setOf(10, 22), // ლ ღ
    setOf(27, 28, 29), // ძ წ ჭ
    setOf(1, 26), // ბ ც
)

/** Группы похожих по звучанию букв: придыхательный/абруптивный (concept.md 2.2) и ღ–გ, ხ–ყ. */
internal val SIMILAR_BY_SOUND: List<Set<Int>> = listOf(
    setOf(21, 9), // ქ კ
    setOf(7, 18), // თ ტ
    setOf(20, 14), // ფ პ
    setOf(26, 28), // ც წ
    setOf(25, 29), // ჩ ჭ
    setOf(22, 2), // ღ გ
    setOf(30, 23), // ხ ყ
)

/** Контент карточек по id буквы. Буквы без записи показываются без ассоциации и слов. Черновик. */
internal val LETTER_CONTENT: Map<Int, LetterContent> = mapOf(
    0 to LetterContent(
        association = LocalizedText(
            ru = "Круглый бочок и хвостик сверху, как у арбуза с веточкой.",
            en = "A round belly with a little tail on top, like a watermelon with a stem.",
        ),
        words = listOf(
            word("არა", "ара", "ara", "нет", "no"),
            word("მადლობა", "мадлоба", "madloba", "спасибо", "thank you"),
            word("აფთიაქი", "аптиаки", "aptiaki", "аптека", "pharmacy"),
            word("ხაჭაპური", "хачапури", "khachapuri", "хачапури", "khachapuri"),
            word("ბანკი", "банки", "banki", "банк", "bank"),
        ),
    ),
    1 to LetterContent(
        association = LocalizedText(
            ru = "Петелька сверху и круглый низ, как у бублика на палочке.",
            en = "A loop on top and a round bottom, like a bagel on a stick.",
        ),
        words = listOf(
            word("ბანკი", "банки", "banki", "банк", "bank"),
            word("ბინა", "бина", "bina", "квартира", "apartment"),
            word("ბავშვი", "бавшви", "bavshvi", "ребёнок", "child"),
        ),
    ),
)
