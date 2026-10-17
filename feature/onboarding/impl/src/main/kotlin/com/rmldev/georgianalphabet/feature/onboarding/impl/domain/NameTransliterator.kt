package com.rmldev.georgianalphabet.feature.onboarding.impl.domain

import com.rmldev.georgianalphabet.feature.onboarding.api.model.TransliteratedFragment
import com.rmldev.georgianalphabet.feature.onboarding.api.usecase.TransliterateNameUseCase
import javax.inject.Inject

/**
 * Транслитерация имён на мхедрули (concept.md 2.5).
 * к/п/т и k/p/t передаются абруптивами კ/პ/ტ, как принято для иностранных имён.
 */
internal class NameTransliterator @Inject constructor() : TransliterateNameUseCase {

    override fun invoke(text: String): List<TransliteratedFragment> {
        val result = mutableListOf<TransliteratedFragment>()
        var i = 0
        while (i < text.length) {
            val digraph = text.substring(i, minOf(i + 2, text.length))
            val digraphGeorgian = LATIN_DIGRAPHS[digraph.lowercase()]
            if (digraph.length == 2 && digraphGeorgian != null) {
                result += TransliteratedFragment(digraph, digraphGeorgian)
                i += 2
                continue
            }

            val char = text[i]
            val previous = result.lastOrNull()?.georgian
            val georgian = when {
                char in KEPT -> char.toString()

                // «Мария» → მარია, «Дмитрий» → დმიტრი: после «ი» йотированная буква не повторяет его
                char.lowercaseChar() in IOTATED && previous?.endsWith(I) == true ->
                    SINGLE[char.lowercaseChar()]?.removePrefix(I)

                else -> SINGLE[char.lowercaseChar()]
            }
            if (!georgian.isNullOrEmpty()) result += TransliteratedFragment(char.toString(), georgian)
            i++
        }
        return result
    }

    private companion object {
        val KEPT = setOf(' ', '-')

        const val I = "ი"

        val IOTATED = setOf('й', 'ё', 'є', 'ї', 'ю', 'я')

        val LATIN_DIGRAPHS = mapOf(
            "sh" to "შ",
            "ch" to "ჩ",
            "zh" to "ჟ",
            "kh" to "ხ",
            "ts" to "ც",
            "dz" to "ძ",
            "gh" to "ღ",
            "ph" to "ფ",
            "th" to "თ",
        )

        // Пустая строка — буква не записывается (ъ, ь)
        val SINGLE = mapOf(
            'а' to "ა", 'б' to "ბ", 'в' to "ვ", 'г' to "გ", 'ґ' to "გ", 'д' to "დ", 'е' to "ე",
            'ё' to "იო", 'є' to "იე", 'ж' to "ჟ", 'з' to "ზ", 'и' to "ი", 'і' to "ი", 'ї' to "იი",
            'й' to "ი", 'к' to "კ", 'л' to "ლ", 'м' to "მ", 'н' to "ნ", 'о' to "ო", 'п' to "პ",
            'р' to "რ", 'с' to "ს", 'т' to "ტ", 'у' to "უ", 'ў' to "უ", 'ф' to "ფ", 'х' to "ხ",
            'ц' to "ც", 'ч' to "ჩ", 'ш' to "შ", 'щ' to "შჩ", 'ъ' to "", 'ы' to "ი", 'ь' to "",
            'э' to "ე", 'ю' to "იუ", 'я' to "ია",
            'a' to "ა", 'b' to "ბ", 'c' to "კ", 'd' to "დ", 'e' to "ე", 'f' to "ფ", 'g' to "გ",
            'h' to "ჰ", 'i' to "ი", 'j' to "ჯ", 'k' to "კ", 'l' to "ლ", 'm' to "მ", 'n' to "ნ",
            'o' to "ო", 'p' to "პ", 'q' to "ქ", 'r' to "რ", 's' to "ს", 't' to "ტ", 'u' to "უ",
            'v' to "ვ", 'w' to "ვ", 'x' to "ქს", 'y' to "ი", 'z' to "ზ",
        )
    }
}
