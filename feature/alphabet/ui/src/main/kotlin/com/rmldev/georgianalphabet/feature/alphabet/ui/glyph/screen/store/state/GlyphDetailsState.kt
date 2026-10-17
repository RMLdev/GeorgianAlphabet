package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state

import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter

internal data class GlyphDetailsState(val glyphId: Int, val letter: Letter? = null)
