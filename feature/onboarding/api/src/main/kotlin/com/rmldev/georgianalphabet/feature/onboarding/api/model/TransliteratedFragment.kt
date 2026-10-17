package com.rmldev.georgianalphabet.feature.onboarding.api.model

/**
 * Кусок исходного имени и его запись мхедрули: «Р» → «რ», «sh» → «შ», «я» → «ია».
 * Пробелы и дефисы приходят как есть (`source == georgian`).
 */
data class TransliteratedFragment(val source: String, val georgian: String)
