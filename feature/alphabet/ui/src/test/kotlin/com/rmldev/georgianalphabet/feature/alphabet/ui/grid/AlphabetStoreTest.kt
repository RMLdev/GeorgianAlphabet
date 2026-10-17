package com.rmldev.georgianalphabet.feature.alphabet.ui.grid

import com.rmldev.georgianalphabet.core.ui.ActorScope
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.AlphabetActor
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetAction
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetEvent
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetSideEffect
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetState
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetStateToUiMapper
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.GlyphLearningProgress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AlphabetStoreTest {

    @Test
    fun `mapper shows all 33 letters with names and stable progress`() {
        val mapper = AlphabetStateToUiMapper()

        val first = mapper(AlphabetState())
        val second = mapper(AlphabetState())

        assertEquals(33, first.glyphs.size)
        assertEquals('ბ', first.glyphs[1].symbol)
        assertEquals("bani", first.glyphs[1].name)
        assertTrue(first.glyphs.all { it.progress == GlyphLearningProgress.NEW })
        assertEquals(first, second)
    }

    @Test
    fun `grid rows are calculated from letters count`() {
        val uiState = AlphabetStateToUiMapper()(AlphabetState())

        assertEquals(4, uiState.gridColumnsCount)
        assertEquals(9, uiState.gridRowsCount)
    }

    @Test
    fun `open glyph action produces navigation side effect`() = runTest {
        val sideEffects = mutableListOf<AlphabetSideEffect>()
        val scope = object : ActorScope<AlphabetState, AlphabetEvent, AlphabetSideEffect> {
            override val state = AlphabetState()
            override val coroutineScope: CoroutineScope = this@runTest
            override fun event(event: AlphabetEvent) = Unit
            override suspend fun sideEffect(effect: AlphabetSideEffect) {
                sideEffects += effect
            }
        }

        with(AlphabetActor()) { scope.executeAction(AlphabetAction.OpenGlyph(5)) }

        assertEquals(listOf<AlphabetSideEffect>(AlphabetSideEffect.OpenGlyph(5)), sideEffects)
    }
}
