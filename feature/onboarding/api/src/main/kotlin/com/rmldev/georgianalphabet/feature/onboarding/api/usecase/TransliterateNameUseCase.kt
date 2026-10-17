package com.rmldev.georgianalphabet.feature.onboarding.api.usecase

import com.rmldev.georgianalphabet.feature.onboarding.api.model.TransliteratedFragment

interface TransliterateNameUseCase {
    /** Имя кириллицей и/или латиницей → фрагменты мхедрули. Неизвестные символы, ъ и ь пропускаются. */
    operator fun invoke(text: String): List<TransliteratedFragment>
}
