package com.rmldev.georgianalphabet.feature.alphabet.impl.data

import com.rmldev.georgianalphabet.feature.alphabet.api.model.ExampleWord
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LocalizedText

/** Ассоциация и примеры слов одной буквы. */
internal data class LetterContent(val association: LocalizedText?, val words: List<ExampleWord>)
