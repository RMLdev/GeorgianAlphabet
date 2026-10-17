package com.rmldev.georgianalphabet.feature.alphabet.api.model

data class LetterProgress(
    val letterId: Int,
    val correct: Int,
    val total: Int,
    val recognizedFontIds: Set<String>,
    val level: LearningLevel,
) {
    companion object {
        fun new(letterId: Int) = LetterProgress(
            letterId = letterId,
            correct = 0,
            total = 0,
            recognizedFontIds = emptySet(),
            level = LearningLevel.NEW,
        )
    }
}
