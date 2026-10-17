package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state

import com.rmldev.georgianalphabet.feature.alphabet.api.model.LearningLevel
import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LetterProgress
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript

/** Плитка буквы: имя и звук на знакомом пользователю алфавите, уровень по прогрессу (нет записи — новая). */
internal fun Letter.toGlyphUi(progress: LetterProgress?, knownScript: KnownScript): GlyphUi = GlyphUi(
    id = id,
    symbol = glyph,
    name = when (knownScript) {
        KnownScript.CYRILLIC -> nameCyrillic
        KnownScript.LATIN -> nameLatin
    },
    sound = when (knownScript) {
        KnownScript.CYRILLIC -> soundCyrillic
        KnownScript.LATIN -> soundLatin
    }.takeUnless { archaic },
    progress = (progress?.level ?: LearningLevel.NEW).toGlyphLearningProgress(),
    lesson = lesson,
)
