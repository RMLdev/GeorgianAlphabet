package com.rmldev.georgianalphabet.feature.alphabet.impl.progress

import com.rmldev.georgianalphabet.feature.alphabet.api.model.LearningLevel
import javax.inject.Inject

/**
 * Заглушка: полная формула (точность + число шрифтов, concept.md 4) придёт вместе с квизами.
 * Пока: нет ответов — новая, есть хоть один верный — знакомая.
 */
internal class LearningLevelCalculator @Inject constructor() {

    // recognizedFontIds войдёт в полную формулу
    @Suppress("UnusedParameter")
    fun level(correct: Int, total: Int, recognizedFontIds: Set<String>): LearningLevel = when {
        total == 0 || correct == 0 -> LearningLevel.NEW
        else -> LearningLevel.FAMILIAR
    }
}
