package com.rmldev.georgianalphabet.feature.alphabet.db

/** Часть общей RoomDatabase из `:app`, нужная фиче алфавита. */
interface AlphabetFeatureDatabase {
    fun getLetterProgressDao(): LetterProgressDao
}
