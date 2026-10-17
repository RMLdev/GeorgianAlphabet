package com.rmldev.georgianalphabet.feature.alphabet.impl.data

import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LetterDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class StaticAlphabetRepository @Inject constructor() : AlphabetRepository {

    private val sorted: List<Letter> = LETTERS.sortedBy { it.order }
    private val byId: Map<Int, Letter> = LETTERS.associateBy { it.id }

    override fun letters(): List<Letter> = sorted

    override fun letter(id: Int): Letter? = byId[id]

    override fun letterDetails(id: Int): LetterDetails? {
        if (id !in byId) return null
        val content = LETTER_CONTENT[id]
        return LetterDetails(
            letterId = id,
            association = content?.association,
            words = content?.words.orEmpty(),
            similarByShape = SIMILAR_BY_SHAPE.similarTo(id),
            similarBySound = SIMILAR_BY_SOUND.similarTo(id),
        )
    }

    /** Буквы из всех групп с [id], без неё самой, в порядке алфавита. */
    private fun List<Set<Int>>.similarTo(id: Int): List<Int> = filter { id in it }
        .flatten()
        .filter { it != id }
        .distinct()
        .sortedBy { byId.getValue(it).order }
}
