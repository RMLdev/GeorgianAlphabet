package com.rmldev.georgianalphabet.feature.alphabet.api.model

/**
 * Справочные данные карточки буквы (concept.md 2.2): ассоциация, примеры слов, похожие буквы.
 * Есть у каждой буквы; у букв без контента списки пустые, а [association] — null.
 *
 * @param similarByShape id букв, похожих по начертанию (concept.md 2.4), без самой буквы.
 * @param similarBySound id букв, похожих по звучанию: пары придыхательный/абруптивный и т.п.
 */
data class LetterDetails(
    val letterId: Int,
    val association: LocalizedText?,
    val words: List<ExampleWord>,
    val similarByShape: List<Int>,
    val similarBySound: List<Int>,
)

/**
 * Пример слова с буквой. Транслитерация на обоих алфавитах: показывается знакомый (concept.md 2.5).
 *
 * @param audioAsset путь к файлу звука в assets или null, пока звука нет.
 */
data class ExampleWord(
    val georgian: String,
    val transliterationCyrillic: String,
    val transliterationLatin: String,
    val translation: LocalizedText,
    val audioAsset: String? = null,
)

/** Текст на языках интерфейса; нужный выбирает UI по текущей локали. */
data class LocalizedText(val ru: String, val en: String)
