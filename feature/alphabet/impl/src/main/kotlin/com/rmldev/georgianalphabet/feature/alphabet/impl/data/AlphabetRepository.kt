package com.rmldev.georgianalphabet.feature.alphabet.impl.data

import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter

internal interface AlphabetRepository {
    fun letters(): List<Letter>
    fun letter(id: Int): Letter?
}
