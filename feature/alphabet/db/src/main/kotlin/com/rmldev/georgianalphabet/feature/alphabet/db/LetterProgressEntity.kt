package com.rmldev.georgianalphabet.feature.alphabet.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

/**
 * Прогресс по одной букве. Буква без записи считается новой.
 * Уровень не хранится: он вычисляется из этих полей, чтобы смена формулы не требовала миграции.
 */
@Entity(tableName = "letter_progress")
@TypeConverters(FontIdsConverter::class)
data class LetterProgressEntity(
    @PrimaryKey
    @ColumnInfo(name = "letter_id")
    val letterId: Int,
    val correct: Int,
    val total: Int,
    /** Шрифты, в которых букву узнали хотя бы раз. */
    @ColumnInfo(name = "recognized_font_ids")
    val recognizedFontIds: Set<String>,
)
