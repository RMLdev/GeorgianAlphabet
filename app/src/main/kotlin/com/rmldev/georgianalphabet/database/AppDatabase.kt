package com.rmldev.georgianalphabet.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rmldev.georgianalphabet.feature.alphabet.db.AlphabetFeatureDatabase
import com.rmldev.georgianalphabet.feature.alphabet.db.LetterProgressEntity

/**
 * Единственная БД приложения: собирает схемы всех фич (см. CLAUDE.md, «База данных»).
 * Схема экспортируется в `app/schemas`; любое изменение — только с поднятием версии и миграцией.
 */
@Database(
    entities = [LetterProgressEntity::class],
    version = 1,
)
internal abstract class AppDatabase :
    RoomDatabase(),
    AlphabetFeatureDatabase
