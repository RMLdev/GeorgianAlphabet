package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state

import com.rmldev.georgianalphabet.feature.alphabet.api.model.LearningLevel

/** Уровни концепции → состояния плитки: новая, знакомая, уверенная, мастер. */
internal fun LearningLevel.toGlyphLearningProgress(): GlyphLearningProgress = when (this) {
    LearningLevel.NEW -> GlyphLearningProgress.NEW
    LearningLevel.FAMILIAR -> GlyphLearningProgress.STARTED
    LearningLevel.CONFIDENT -> GlyphLearningProgress.ADVANCED
    LearningLevel.MASTER -> GlyphLearningProgress.MASTERED
}
