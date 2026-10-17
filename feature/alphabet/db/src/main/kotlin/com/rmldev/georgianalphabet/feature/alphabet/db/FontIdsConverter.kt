package com.rmldev.georgianalphabet.feature.alphabet.db

import androidx.room.TypeConverter

/** Набор id шрифтов хранится одной строкой через запятую (id шрифтов — латиница без запятых). */
class FontIdsConverter {

    @TypeConverter
    fun fromSet(ids: Set<String>): String = ids.sorted().joinToString(SEPARATOR)

    @TypeConverter
    fun toSet(value: String): Set<String> = value.split(SEPARATOR).filter { it.isNotEmpty() }.toSet()

    private companion object {
        const val SEPARATOR = ","
    }
}
