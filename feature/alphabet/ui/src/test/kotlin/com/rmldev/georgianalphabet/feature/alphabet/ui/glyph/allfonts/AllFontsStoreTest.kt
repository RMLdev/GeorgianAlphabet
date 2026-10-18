package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts

import com.rmldev.georgianalphabet.core.designsystem.font.FontCatalog
import com.rmldev.georgianalphabet.core.ui.ActorScope
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LearningLevel
import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LetterProgress
import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.GetLetterUseCase
import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.ObserveLettersProgressUseCase
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.AllFontsActor
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.AllFontsReducer
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.message.AllFontsAction
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.message.AllFontsEvent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.message.AllFontsSideEffect
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.state.AllFontsState
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.state.AllFontsStateToUiMapper
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.state.AllFontsUiState
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript
import com.rmldev.georgianalphabet.feature.preferences.api.model.UserPreferences
import com.rmldev.georgianalphabet.feature.preferences.api.usecase.ObserveUserPreferencesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private val ANI = Letter(
    id = 0, glyph = 'ა', nameGeorgian = "ანი", nameCyrillic = "ани", nameLatin = "ani",
    soundCyrillic = "а", soundLatin = "a", ipa = "ɑ", lesson = 1, archaic = false, order = 1,
)

private class FakeGetLetter(private val letters: List<Letter>) : GetLetterUseCase {
    override suspend fun invoke(id: Int): Letter? = letters.firstOrNull { it.id == id }
}

private class FakeObserveProgress(private val flow: Flow<Map<Int, LetterProgress>>) : ObserveLettersProgressUseCase {
    override fun invoke(): Flow<Map<Int, LetterProgress>> = flow
}

private class FakeObservePreferences(private val flow: Flow<UserPreferences>) : ObserveUserPreferencesUseCase {
    override fun invoke(): Flow<UserPreferences> = flow
}

private fun preferences(script: KnownScript) =
    UserPreferences(onboardingCompleted = true, knownScript = script, userName = null)

private class RecordingScope(override val state: AllFontsState, override val coroutineScope: CoroutineScope) :
    ActorScope<AllFontsState, AllFontsEvent, AllFontsSideEffect> {
    val events = mutableListOf<AllFontsEvent>()
    val sideEffects = mutableListOf<AllFontsSideEffect>()
    override fun event(event: AllFontsEvent) {
        events += event
    }
    override suspend fun sideEffect(effect: AllFontsSideEffect) {
        sideEffects += effect
    }
}

class AllFontsStoreTest {

    private val reducer = AllFontsReducer()
    private val mapper = AllFontsStateToUiMapper()

    private fun actor(
        progress: Flow<Map<Int, LetterProgress>> = flowOf(emptyMap()),
        preferences: Flow<UserPreferences> = flowOf(preferences(KnownScript.CYRILLIC)),
    ) = AllFontsActor(FakeGetLetter(listOf(ANI)), FakeObserveProgress(progress), FakeObservePreferences(preferences))

    private suspend fun AllFontsActor.execute(scope: RecordingScope, action: AllFontsAction) =
        with(this) { scope.executeAction(action) }

    private fun loaded(script: KnownScript = KnownScript.CYRILLIC, progress: LetterProgress? = null) = listOf(
        AllFontsEvent.LetterLoaded(ANI),
        AllFontsEvent.ProgressUpdated(progress, script),
    ).fold(AllFontsState(0), reducer::reduce)

    // region Actor

    @Test
    fun `load letter emits the letter or null for unknown id`() = runTest {
        val scope = RecordingScope(AllFontsState(0), this)
        val unknownScope = RecordingScope(AllFontsState(99), this)

        actor().execute(scope, AllFontsAction.LoadLetter)
        actor().execute(unknownScope, AllFontsAction.LoadLetter)

        assertEquals(listOf<AllFontsEvent>(AllFontsEvent.LetterLoaded(ANI)), scope.events)
        assertEquals(listOf<AllFontsEvent>(AllFontsEvent.LetterLoaded(null)), unknownScope.events)
    }

    @Test
    fun `observe progress follows this letter and known script`() = runTest {
        val progress = MutableStateFlow(mapOf(1 to LetterProgress.new(1)))
        val preferences = MutableStateFlow(preferences(KnownScript.CYRILLIC))
        val scope = RecordingScope(AllFontsState(0), this)
        val actor = actor(progress = progress, preferences = preferences)

        val job = launch { actor.execute(scope, AllFontsAction.ObserveProgress) }
        runCurrent()
        // Progress of another letter doesn't touch this screen
        progress.value = mapOf(1 to LetterProgress.new(1).copy(total = 3))
        runCurrent()
        val aniProgress = LetterProgress.new(0).copy(recognizedFontIds = setOf("k1fs"))
        progress.value = mapOf(0 to aniProgress)
        runCurrent()
        preferences.value = preferences(KnownScript.LATIN)
        runCurrent()
        job.cancel()

        assertEquals(
            listOf<AllFontsEvent>(
                AllFontsEvent.ProgressUpdated(null, KnownScript.CYRILLIC),
                AllFontsEvent.ProgressUpdated(aniProgress, KnownScript.CYRILLIC),
                AllFontsEvent.ProgressUpdated(aniProgress, KnownScript.LATIN),
            ),
            scope.events,
        )
    }

    @Test
    fun `open font returns to the card only for an open font`() = runTest {
        val scope = RecordingScope(AllFontsState(0), this)
        val actor = actor()

        actor.execute(scope, AllFontsAction.OpenFont("dejavu_sans"))
        // Aznauri is locked until a 30-day streak
        actor.execute(scope, AllFontsAction.OpenFont("aznauri"))
        actor.execute(scope, AllFontsAction.Back)

        assertEquals(
            listOf(AllFontsSideEffect.OpenFont(0, "dejavu_sans"), AllFontsSideEffect.Back),
            scope.sideEffects,
        )
        assertTrue(scope.events.isEmpty())
    }

    @Test
    fun `open font accepts a font unlocked as a reward`() = runTest {
        val scope = RecordingScope(AllFontsState(0, unlockedFontIds = setOf("aznauri")), this)

        actor().execute(scope, AllFontsAction.OpenFont("aznauri"))

        assertEquals(listOf<AllFontsSideEffect>(AllFontsSideEffect.OpenFont(0, "aznauri")), scope.sideEffects)
    }

    // endregion

    // region Mapper

    @Test
    fun `mapper is empty while loading and marks unknown id as not found`() {
        assertEquals(AllFontsUiState(letterId = 0), mapper(AllFontsState(0)))
        assertFalse(mapper(AllFontsState(0)).isNotFound)

        val notFound = mapper(reducer.reduce(AllFontsState(99), AllFontsEvent.LetterLoaded(null)))
        assertTrue(notFound.isNotFound)
        assertTrue(notFound.groups.isEmpty())
    }

    @Test
    fun `mapper groups every catalog font by difficulty`() {
        val ui = mapper(loaded())

        assertEquals("ა", ui.symbol)
        assertEquals("ани", ui.letterName)
        assertEquals(FontCatalog.all.size, ui.totalCount)
        assertEquals(FontCatalog.unlocked().size, ui.openCount)
        assertEquals(listOf(1, 2, 3), ui.groups.map { it.difficulty })
        assertEquals(FontCatalog.all.map { it.id }, ui.groups.flatMap { group -> group.fonts.map { it.font.id } })
        ui.groups.forEach { group ->
            assertTrue(group.fonts.all { it.font.difficulty == group.difficulty })
            assertEquals(group.fonts.count { it.isOpen }, group.openCount)
        }
        assertTrue(ui.groups.flatMap { it.fonts }.all { it.isOpen == it.font.unlockedByDefault })
    }

    @Test
    fun `mapper marks recognized open fonts only`() {
        val progress = LetterProgress(
            letterId = 0,
            correct = 5,
            total = 6,
            // aznauri is locked, unknown_font isn't in the catalog
            recognizedFontIds = setOf("dejavu_sans", "aznauri", "unknown_font"),
            level = LearningLevel.FAMILIAR,
        )

        val tiles = mapper(loaded(progress = progress)).groups.flatMap { it.fonts }

        assertEquals(listOf("dejavu_sans"), tiles.filter { it.isRecognized }.map { it.font.id })
    }

    @Test
    fun `mapper counts reward fonts as open and names the letter in the known script`() {
        val ui = mapper(loaded(script = KnownScript.LATIN).copy(unlockedFontIds = setOf("aznauri")))

        assertEquals("ani", ui.letterName)
        assertEquals(FontCatalog.unlocked().size + 1, ui.openCount)
        assertTrue(ui.groups.flatMap { it.fonts }.first { it.font.id == "aznauri" }.isOpen)
    }

    // endregion
}
