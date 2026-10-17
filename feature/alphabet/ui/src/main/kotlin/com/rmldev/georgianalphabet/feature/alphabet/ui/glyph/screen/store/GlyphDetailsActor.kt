package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store

import com.rmldev.georgianalphabet.core.ui.ActorScope
import com.rmldev.georgianalphabet.core.ui.MviActor
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsAction
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsEvent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsSideEffect
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.GlyphDetailsState
import javax.inject.Inject

private typealias GlyphDetailsActorScope = ActorScope<GlyphDetailsState, GlyphDetailsEvent, GlyphDetailsSideEffect>

internal class GlyphDetailsActor @Inject constructor() :
    MviActor<GlyphDetailsAction, GlyphDetailsState, GlyphDetailsEvent, GlyphDetailsSideEffect> {

    override suspend fun GlyphDetailsActorScope.executeAction(
        action: GlyphDetailsAction
    ) {

    }
}
