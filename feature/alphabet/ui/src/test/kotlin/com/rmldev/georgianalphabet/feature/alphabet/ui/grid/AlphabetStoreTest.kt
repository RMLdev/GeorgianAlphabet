package com.rmldev.georgianalphabet.feature.alphabet.ui.grid

import com.rmldev.georgianalphabet.core.ui.ActorScope
import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter
import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.GetLettersUseCase
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.AlphabetActor
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.AlphabetReducer
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

private fun letter(
    id: Int,
    glyph: Char,
    name: String,
    archaic: Boolean = false,
    order: Int = id + 1,
    sound: String = "",
) = Letter(
    id = id, glyph = glyph, nameGeorgian = "", nameCyrillic = "", nameLatin = name,
    soundCyrillic = "", soundLatin = sound, ipa = "", lesson = if (archaic) null else 1,
    archaic = archaic, order = order,
)

private class FakeGetLetters(private val letters: List<Letter>) : GetLettersUseCase {
    override suspend fun invoke(): List<Letter> = letters
}

private class RecordingScope(override val state: AlphabetState, override val coroutineScope: CoroutineScope) :
    ActorScope<AlphabetState, AlphabetEvent, AlphabetSideEffect> {
    val events = mutableListOf<AlphabetEvent>()
    val sideEffects = mutableListOf<AlphabetSideEffect>()
    override fun event(event: AlphabetEvent) {
        events += event
    }
    override suspend fun sideEffect(effect: AlphabetSideEffect) {
        sideEffects += effect
    }
}

class AlphabetStoreTest {

    private val a = letter(0, 'ა', "ani")
    private val b = letter(1, 'ბ', "bani")

    @Test
    fun `mapper maps letters to glyphs`() {
        val mapper = AlphabetStateToUiMapper()

        val first = mapper(AlphabetState(listOf(a, b)))
        val second = mapper(AlphabetState(listOf(a, b)))

        assertEquals(2, first.glyphs.size)
        assertEquals('ბ', first.glyphs[1].symbol)
        assertEquals("bani", first.glyphs[1].name)
        assertEquals(1, first.glyphs[1].id)
        assertTrue(first.glyphs.all { it.progress == GlyphLearningProgress.NEW })
        assertEquals(first, second)
    }

    @Test
    fun `mapper keeps sound for regular letters and drops it for archaic ones`() {
        val regular = letter(22, 'ღ', "ghani", sound = "gh")
        val archaic = letter(33, 'ჱ', "ei", archaic = true, sound = "ei")

        val ui = AlphabetStateToUiMapper()(AlphabetState(listOf(regular, archaic)))

        assertEquals("gh", ui.glyphs[0].sound)
        assertEquals(null, ui.glyphs[1].sound)
    }

    @Test
    fun `mapper handles empty state`() {
        val ui = AlphabetStateToUiMapper()(AlphabetState())

        assertTrue(ui.glyphs.isEmpty())
        assertEquals(0, ui.gridRowsCount)
    }

    @Test
    fun `grid rows are calculated from letters count`() {
        val letters = (0 until 33).map { letter(it, 'ა', "x") }
        val ui = AlphabetStateToUiMapper()(AlphabetState(letters))

        assertEquals(4, ui.gridColumnsCount)
        assertEquals(9, ui.gridRowsCount)
    }

    @Test
    fun `reducer stores loaded letters`() {
        val result = AlphabetReducer().reduce(AlphabetState(), AlphabetEvent.LettersLoaded(listOf(a, b)))

        assertEquals(listOf(a, b), result.letters)
    }

    @Test
    fun `load letters emits modern letters sorted by order`() = runTest {
        val archaic = letter(33, 'ჱ', "hae", archaic = true, order = 7)
        val late = letter(2, 'გ', "gani", order = 3)
        val scope = RecordingScope(AlphabetState(), this)

        with(AlphabetActor(FakeGetLetters(listOf(late, archaic, a)))) {
            scope.executeAction(AlphabetAction.LoadLetters)
        }

        assertEquals(listOf<AlphabetEvent>(AlphabetEvent.LettersLoaded(listOf(a, late))), scope.events)
        assertTrue(scope.sideEffects.isEmpty())
    }

    @Test
    fun `open glyph action produces navigation side effect`() = runTest {
        val scope = RecordingScope(AlphabetState(), this)

        with(AlphabetActor(FakeGetLetters(emptyList()))) { scope.executeAction(AlphabetAction.OpenGlyph(5)) }

        assertEquals(listOf<AlphabetSideEffect>(AlphabetSideEffect.OpenGlyph(5)), scope.sideEffects)
        assertTrue(scope.events.isEmpty())
    }
}
