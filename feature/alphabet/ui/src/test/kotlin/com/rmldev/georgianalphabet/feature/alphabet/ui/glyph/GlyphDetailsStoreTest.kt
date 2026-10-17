package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph

import com.rmldev.georgianalphabet.core.ui.ActorScope
import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter
import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.GetLetterUseCase
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.GlyphDetailsActor
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.GlyphDetailsReducer
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsAction
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsEvent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsSideEffect
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.GlyphDetailsState
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.GlyphDetailsStateToUiMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private val LETTER = Letter(
    id = 1, glyph = 'ბ', nameGeorgian = "ბანი", nameCyrillic = "бани", nameLatin = "bani",
    soundCyrillic = "б", soundLatin = "b", ipa = "b", lesson = 1, archaic = false, order = 2,
)

private class FakeGetLetter(private val letters: List<Letter>) : GetLetterUseCase {
    val requested = mutableListOf<Int>()
    override suspend fun invoke(id: Int): Letter? {
        requested += id
        return letters.firstOrNull { it.id == id }
    }
}

private class RecordingScope(override val state: GlyphDetailsState, override val coroutineScope: CoroutineScope) :
    ActorScope<GlyphDetailsState, GlyphDetailsEvent, GlyphDetailsSideEffect> {
    val events = mutableListOf<GlyphDetailsEvent>()
    override fun event(event: GlyphDetailsEvent) {
        events += event
    }
    override suspend fun sideEffect(effect: GlyphDetailsSideEffect) = Unit
}

class GlyphDetailsStoreTest {

    @Test
    fun `reducer stores loaded letter`() {
        val result = GlyphDetailsReducer().reduce(GlyphDetailsState(1), GlyphDetailsEvent.LetterLoaded(LETTER))

        assertEquals(LETTER, result.letter)
        assertEquals(1, result.glyphId)
    }

    @Test
    fun `reducer accepts null letter`() {
        val result = GlyphDetailsReducer().reduce(
            GlyphDetailsState(1, LETTER),
            GlyphDetailsEvent.LetterLoaded(null),
        )

        assertEquals(null, result.letter)
    }

    @Test
    fun `mapper is empty until letter is loaded`() {
        val ui = GlyphDetailsStateToUiMapper()(GlyphDetailsState(1))

        assertEquals("", ui.symbol)
        assertEquals("", ui.name)
    }

    @Test
    fun `mapper maps loaded letter`() {
        val ui = GlyphDetailsStateToUiMapper()(GlyphDetailsState(1, LETTER))

        assertEquals("ბ", ui.symbol)
        assertEquals("bani", ui.name)
    }

    @Test
    fun `load letter requests letter by state id`() = runTest {
        val fake = FakeGetLetter(listOf(LETTER))
        val scope = RecordingScope(GlyphDetailsState(1), this)

        with(GlyphDetailsActor(fake)) { scope.executeAction(GlyphDetailsAction.LoadLetter) }

        assertEquals(listOf(1), fake.requested)
        assertEquals(listOf<GlyphDetailsEvent>(GlyphDetailsEvent.LetterLoaded(LETTER)), scope.events)
    }

    @Test
    fun `load letter emits null for unknown id`() = runTest {
        val scope = RecordingScope(GlyphDetailsState(99), this)

        with(GlyphDetailsActor(FakeGetLetter(listOf(LETTER)))) {
            scope.executeAction(GlyphDetailsAction.LoadLetter)
        }

        assertEquals(listOf<GlyphDetailsEvent>(GlyphDetailsEvent.LetterLoaded(null)), scope.events)
    }

    @Test
    fun `stub action does nothing`() = runTest {
        val scope = RecordingScope(GlyphDetailsState(1), this)

        with(GlyphDetailsActor(FakeGetLetter(emptyList()))) { scope.executeAction(GlyphDetailsAction.Stub) }

        assertTrue(scope.events.isEmpty())
    }
}
