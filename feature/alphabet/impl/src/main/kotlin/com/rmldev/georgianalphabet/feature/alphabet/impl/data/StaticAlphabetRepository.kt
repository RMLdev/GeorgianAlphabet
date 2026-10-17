package com.rmldev.georgianalphabet.feature.alphabet.impl.data

import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class StaticAlphabetRepository @Inject constructor() : AlphabetRepository {

    private val sorted: List<Letter> = LETTERS.sortedBy { it.order }
    private val byId: Map<Int, Letter> = LETTERS.associateBy { it.id }

    override fun letters(): List<Letter> = sorted

    override fun letter(id: Int): Letter? = byId[id]
}
