package com.rmldev.georgianalphabet.feature.alphabet.ui.grid

import com.rmldev.georgianalphabet.core.ui.ActorScope
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LearningLevel
import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LetterProgress
import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.GetLettersUseCase
import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.ObserveLettersProgressUseCase
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.AlphabetActor
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.AlphabetReducer
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetAction
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetEvent
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.message.AlphabetSideEffect
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetGridItem
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetState
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetStateToUiMapper
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.GlyphLearningProgress
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript
import com.rmldev.georgianalphabet.feature.preferences.api.model.UserPreferences
import com.rmldev.georgianalphabet.feature.preferences.api.usecase.ObserveUserPreferencesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

private fun letter(id: Int, glyph: Char, archaic: Boolean = false, order: Int = id + 1) = Letter(
    id = id, glyph = glyph, nameGeorgian = "", nameCyrillic = "имя$id", nameLatin = "name$id",
    soundCyrillic = "з$id", soundLatin = "s$id", ipa = "", lesson = if (archaic) null else 1,
    archaic = archaic, order = order,
)

private fun progress(id: Int, level: LearningLevel) = LetterProgress.new(id).copy(level = level)

private fun preferences(script: KnownScript) =
    UserPreferences(onboardingCompleted = true, knownScript = script, userName = null)

private class FakeGetLetters(private val letters: List<Letter>) : GetLettersUseCase {
    override suspend fun invoke(): List<Letter> = letters
}

private class FakeObserveProgress(private val flow: Flow<Map<Int, LetterProgress>>) : ObserveLettersProgressUseCase {
    override fun invoke(): Flow<Map<Int, LetterProgress>> = flow
}

private class FakeObservePreferences(private val flow: Flow<UserPreferences>) : ObserveUserPreferencesUseCase {
    override fun invoke(): Flow<UserPreferences> = flow
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

    private val a = letter(0, 'ა')
    private val b = letter(1, 'ბ')
    private val archaic = letter(33, 'ჱ', archaic = true, order = 8)

    private val mapper = AlphabetStateToUiMapper()

    private fun actor(
        letters: List<Letter> = emptyList(),
        progress: Flow<Map<Int, LetterProgress>> = flowOf(emptyMap()),
        preferences: Flow<UserPreferences> = flowOf(preferences(KnownScript.CYRILLIC)),
    ) = AlphabetActor(FakeGetLetters(letters), FakeObserveProgress(progress), FakeObservePreferences(preferences))

    @Test
    fun `mapper signs letters on cyrillic`() {
        val ui = mapper(AlphabetState(letters = listOf(a, b), knownScript = KnownScript.CYRILLIC))

        val glyph = (ui.gridItems[1] as AlphabetGridItem.Glyph).glyph
        assertEquals(1, glyph.id)
        assertEquals('ბ', glyph.symbol)
        assertEquals("имя1", glyph.name)
        assertEquals("з1", glyph.sound)
    }

    @Test
    fun `mapper signs letters on latin`() {
        val ui = mapper(AlphabetState(letters = listOf(a), knownScript = KnownScript.LATIN))

        val glyph = (ui.gridItems.single() as AlphabetGridItem.Glyph).glyph
        assertEquals("name0", glyph.name)
        assertEquals("s0", glyph.sound)
    }

    @Test
    fun `letters without progress are new, others take their level`() {
        val ui = mapper(
            AlphabetState(letters = listOf(a, b), progress = mapOf(1 to progress(1, LearningLevel.CONFIDENT))),
        )

        val levels = ui.gridItems.map { (it as AlphabetGridItem.Glyph).glyph.progress }
        assertEquals(listOf(GlyphLearningProgress.NEW, GlyphLearningProgress.ADVANCED), levels)
    }

    @Test
    fun `archaic letters go to their own block without sound and are not counted`() {
        val ui = mapper(
            AlphabetState(
                letters = listOf(a, archaic, b),
                progress = mapOf(
                    0 to progress(0, LearningLevel.MASTER),
                    33 to progress(33, LearningLevel.MASTER),
                ),
            ),
        )

        assertEquals(listOf(0, 1), ui.gridItems.map { (it as AlphabetGridItem.Glyph).glyph.id })
        assertEquals(33, ui.archaicGlyphs.single().id)
        assertNull(ui.archaicGlyphs.single().sound)
        assertEquals(2, ui.lettersCount)
        assertEquals(1, ui.masteredCount)
    }

    @Test
    fun `mapper handles empty state`() {
        val ui = mapper(AlphabetState())

        assertTrue(ui.gridItems.isEmpty())
        assertTrue(ui.archaicGlyphs.isEmpty())
        assertEquals(0, ui.lettersCount)
        assertEquals(0, ui.masteredCount)
    }

    @Test
    fun `reducer stores letters, progress and script`() {
        val progress = mapOf(0 to progress(0, LearningLevel.FAMILIAR))

        val result = AlphabetReducer().reduce(
            AlphabetState(),
            AlphabetEvent.LettersUpdated(listOf(a, b), progress, KnownScript.LATIN),
        )

        assertEquals(AlphabetState(listOf(a, b), progress, KnownScript.LATIN), result)
    }

    @Test
    fun `observe letters emits sorted letters on every progress or script change`() = runTest {
        val late = letter(2, 'გ', order = 3)
        val progressFlow = MutableStateFlow<Map<Int, LetterProgress>>(emptyMap())
        val preferencesFlow = MutableStateFlow(preferences(KnownScript.CYRILLIC))
        val scope = RecordingScope(AlphabetState(), this)
        val sorted = listOf(a, archaic, late)

        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            with(actor(listOf(late, archaic, a), progressFlow, preferencesFlow)) {
                scope.executeAction(AlphabetAction.ObserveLetters)
            }
        }
        val progress = mapOf(0 to progress(0, LearningLevel.MASTER))
        progressFlow.value = progress
        // userName change doesn't touch the alphabet
        preferencesFlow.value = preferencesFlow.value.copy(userName = "Роман")
        preferencesFlow.value = preferences(KnownScript.LATIN)
        job.cancel()

        assertEquals(
            listOf<AlphabetEvent>(
                AlphabetEvent.LettersUpdated(sorted, emptyMap(), KnownScript.CYRILLIC),
                AlphabetEvent.LettersUpdated(sorted, progress, KnownScript.CYRILLIC),
                AlphabetEvent.LettersUpdated(sorted, progress, KnownScript.LATIN),
            ),
            scope.events,
        )
        assertTrue(scope.sideEffects.isEmpty())
    }

    @Test
    fun `open glyph action produces navigation side effect`() = runTest {
        val scope = RecordingScope(AlphabetState(), this)

        with(actor()) { scope.executeAction(AlphabetAction.OpenGlyph(5)) }

        assertEquals(listOf<AlphabetSideEffect>(AlphabetSideEffect.OpenGlyph(5)), scope.sideEffects)
        assertTrue(scope.events.isEmpty())
    }
}
