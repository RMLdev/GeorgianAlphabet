package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph

import androidx.lifecycle.SavedStateHandle
import com.rmldev.georgianalphabet.core.audio.api.AudioAssets
import com.rmldev.georgianalphabet.core.audio.api.SoundNotFoundException
import com.rmldev.georgianalphabet.core.audio.api.SoundPlayer
import com.rmldev.georgianalphabet.core.designsystem.font.FontCatalog
import com.rmldev.georgianalphabet.core.ui.ActorScope
import com.rmldev.georgianalphabet.feature.alphabet.api.model.ExampleWord
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LearningLevel
import com.rmldev.georgianalphabet.feature.alphabet.api.model.Letter
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LetterDetails
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LetterProgress
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LocalizedText
import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.GetLetterDetailsUseCase
import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.GetLettersUseCase
import com.rmldev.georgianalphabet.feature.alphabet.api.usecase.ObserveLettersProgressUseCase
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.GlyphDetailsActor
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.GlyphDetailsReducer
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsAction
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsEvent
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.message.GlyphDetailsSideEffect
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.FontCellStatus
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.GlyphDetailsState
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.GlyphDetailsStateToUiMapper
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.GlyphDetailsUiState
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.SimilarLetterUi
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.WordPart
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.restoreFontId
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.saveFontId
import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state.splitByLetter
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript
import com.rmldev.georgianalphabet.feature.preferences.api.model.UserPreferences
import com.rmldev.georgianalphabet.feature.preferences.api.usecase.ObserveUserPreferencesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

private fun letter(id: Int, glyph: Char, archaic: Boolean = false) = Letter(
    id = id, glyph = glyph, nameGeorgian = "ka$id", nameCyrillic = "имя$id", nameLatin = "name$id",
    soundCyrillic = "з$id", soundLatin = "s$id", ipa = "ipa$id", lesson = if (archaic) null else 1,
    archaic = archaic, order = id + 1,
)

private val ANI = letter(0, 'ა')
private val SANI = letter(17, 'ს')
private val KANI = letter(9, 'კ')
private val ARCHAIC = letter(33, 'ჱ', archaic = true)

private val ANI_DETAILS = LetterDetails(
    letterId = 0,
    association = LocalizedText("арбуз", "watermelon"),
    words = listOf(
        ExampleWord("არა", "ара", "ara", LocalizedText("нет", "no"), audioAsset = "audio/words/ara.mp3"),
    ),
    similarByShape = listOf(17),
    similarBySound = listOf(9, 99),
)

private class FakeGetLetters(private val letters: List<Letter>) : GetLettersUseCase {
    override suspend fun invoke(): List<Letter> = letters
}

private class FakeGetLetterDetails(private val details: List<LetterDetails>) : GetLetterDetailsUseCase {
    val requested = mutableListOf<Int>()
    override suspend fun invoke(id: Int): LetterDetails? {
        requested += id
        return details.firstOrNull { it.letterId == id }
    }
}

private class FakeObserveProgress(private val flow: Flow<Map<Int, LetterProgress>>) : ObserveLettersProgressUseCase {
    override fun invoke(): Flow<Map<Int, LetterProgress>> = flow
}

private class FakeObservePreferences(private val flow: Flow<UserPreferences>) : ObserveUserPreferencesUseCase {
    override fun invoke(): Flow<UserPreferences> = flow
}

/** Plays only [available] paths, like assets without the missing files. */
private class FakeSoundPlayer(private val available: Set<String> = emptySet()) : SoundPlayer {
    private val mutablePlaying = MutableStateFlow<String?>(null)
    override val playing: StateFlow<String?> = mutablePlaying
    val requested = mutableListOf<String>()
    var stops = 0

    override suspend fun play(assetPath: String): Result<Unit> {
        requested += assetPath
        mutablePlaying.value = null
        if (assetPath !in available) return Result.failure(SoundNotFoundException(assetPath))
        mutablePlaying.value = assetPath
        return Result.success(Unit)
    }

    override fun stop() {
        stops++
        mutablePlaying.value = null
    }

    fun finish() {
        mutablePlaying.value = null
    }
}

private fun preferences(script: KnownScript) =
    UserPreferences(onboardingCompleted = true, knownScript = script, userName = null)

private class RecordingScope(override val state: GlyphDetailsState, override val coroutineScope: CoroutineScope) :
    ActorScope<GlyphDetailsState, GlyphDetailsEvent, GlyphDetailsSideEffect> {
    val events = mutableListOf<GlyphDetailsEvent>()
    val sideEffects = mutableListOf<GlyphDetailsSideEffect>()
    override fun event(event: GlyphDetailsEvent) {
        events += event
    }
    override suspend fun sideEffect(effect: GlyphDetailsSideEffect) {
        sideEffects += effect
    }
}

class GlyphDetailsStoreTest {

    private val reducer = GlyphDetailsReducer()
    private val mapper = GlyphDetailsStateToUiMapper()
    private val details = FakeGetLetterDetails(listOf(ANI_DETAILS))
    private val player = FakeSoundPlayer(setOf(AudioAssets.letter(0), "audio/words/ara.mp3"))

    private fun actor(
        letters: List<Letter> = listOf(ANI, SANI, KANI, ARCHAIC),
        progress: Flow<Map<Int, LetterProgress>> = flowOf(emptyMap()),
        preferences: Flow<UserPreferences> = flowOf(preferences(KnownScript.CYRILLIC)),
    ) = GlyphDetailsActor(
        FakeGetLetters(letters),
        details,
        FakeObserveProgress(progress),
        FakeObservePreferences(preferences),
        player,
    )

    private suspend fun GlyphDetailsActor.execute(scope: RecordingScope, action: GlyphDetailsAction) =
        with(this) { scope.executeAction(action) }

    private fun loaded(
        letter: Letter? = ANI,
        letterDetails: LetterDetails? = ANI_DETAILS,
        script: KnownScript = KnownScript.CYRILLIC,
        progress: LetterProgress? = null,
    ) = listOf(
        GlyphDetailsEvent.LetterLoaded(letter, letterDetails, listOf(SANI, KANI)),
        GlyphDetailsEvent.ProgressUpdated(progress, script),
    ).fold(GlyphDetailsState(letter?.id ?: 99), reducer::reduce)

    // region Actor

    @Test
    fun `load letter emits letter, details and related letters`() = runTest {
        val scope = RecordingScope(GlyphDetailsState(0), this)

        actor().execute(scope, GlyphDetailsAction.LoadLetter)

        assertEquals(listOf(0), details.requested)
        // 99 isn't a letter, so it is skipped
        assertEquals(
            listOf<GlyphDetailsEvent>(GlyphDetailsEvent.LetterLoaded(ANI, ANI_DETAILS, listOf(SANI, KANI))),
            scope.events,
        )
    }

    @Test
    fun `load letter emits nulls for unknown id without asking details`() = runTest {
        val scope = RecordingScope(GlyphDetailsState(99), this)

        actor().execute(scope, GlyphDetailsAction.LoadLetter)

        assertTrue(details.requested.isEmpty())
        assertEquals(
            listOf<GlyphDetailsEvent>(GlyphDetailsEvent.LetterLoaded(null, null, emptyList())),
            scope.events,
        )
    }

    @Test
    fun `observe progress follows this letter and known script`() = runTest {
        val progress = MutableStateFlow(mapOf(1 to LetterProgress.new(1)))
        val preferences = MutableStateFlow(preferences(KnownScript.CYRILLIC))
        val scope = RecordingScope(GlyphDetailsState(0), this)

        val actor = actor(progress = progress, preferences = preferences)

        val job = launch { actor.execute(scope, GlyphDetailsAction.Observe) }
        runCurrent()
        // Progress of another letter doesn't touch this card
        progress.value = mapOf(1 to LetterProgress.new(1).copy(total = 3))
        runCurrent()
        val aniProgress = LetterProgress.new(0).copy(correct = 1, total = 2)
        progress.value = mapOf(0 to aniProgress)
        runCurrent()
        preferences.value = preferences(KnownScript.LATIN)
        runCurrent()
        job.cancel()

        assertEquals(
            listOf<GlyphDetailsEvent>(
                GlyphDetailsEvent.ProgressUpdated(null, KnownScript.CYRILLIC),
                GlyphDetailsEvent.ProgressUpdated(aniProgress, KnownScript.CYRILLIC),
                GlyphDetailsEvent.ProgressUpdated(aniProgress, KnownScript.LATIN),
            ),
            scope.events.filterIsInstance<GlyphDetailsEvent.ProgressUpdated>(),
        )
    }

    @Test
    fun `clicks become navigation side effects`() = runTest {
        val scope = RecordingScope(GlyphDetailsState(0), this)
        val actor = actor()

        actor.execute(scope, GlyphDetailsAction.OpenLetter(17))
        actor.execute(scope, GlyphDetailsAction.OpenAllFonts)
        actor.execute(scope, GlyphDetailsAction.Back)

        assertEquals(
            listOf(
                GlyphDetailsSideEffect.OpenLetter(17),
                GlyphDetailsSideEffect.OpenAllFonts(0),
                GlyphDetailsSideEffect.Back,
            ),
            scope.sideEffects,
        )
        assertTrue(scope.events.isEmpty())
        // Leaving the card silences it
        assertEquals(3, player.stops)
    }

    @Test
    fun `play name plays letter asset`() = runTest {
        val scope = RecordingScope(GlyphDetailsState(0), this)

        actor().execute(scope, GlyphDetailsAction.PlayName)

        assertEquals(listOf(AudioAssets.letter(0)), player.requested)
        assertEquals(AudioAssets.letter(0), player.playing.value)
    }

    @Test
    fun `play word plays its asset and skips words without one`() = runTest {
        val noAudio = ANI_DETAILS.copy(
            words = ANI_DETAILS.words + ExampleWord("ბანი", "бани", "bani", LocalizedText("бани", "bani")),
        )
        val scope = RecordingScope(GlyphDetailsState(0, details = noAudio), this)
        val actor = actor()

        actor.execute(scope, GlyphDetailsAction.PlayWord("ბანი"))
        actor.execute(scope, GlyphDetailsAction.PlayWord("უცნობი"))
        actor.execute(scope, GlyphDetailsAction.PlayWord("არა"))

        assertEquals(listOf("audio/words/ara.mp3"), player.requested)
    }

    @Test
    fun `missing sound file is silent`() = runTest {
        val scope = RecordingScope(GlyphDetailsState(5), this)

        actor().execute(scope, GlyphDetailsAction.PlayName)

        assertEquals(listOf(AudioAssets.letter(5)), player.requested)
        assertNull(player.playing.value)
        assertTrue(scope.events.isEmpty())
        assertTrue(scope.sideEffects.isEmpty())
    }

    @Test
    fun `observe follows playing sound`() = runTest {
        val scope = RecordingScope(GlyphDetailsState(0), this)
        val actor = actor()

        val job = launch { actor.execute(scope, GlyphDetailsAction.Observe) }
        runCurrent()
        actor.execute(scope, GlyphDetailsAction.PlayName)
        runCurrent()
        player.finish()
        runCurrent()
        job.cancel()

        assertEquals(
            listOf(
                GlyphDetailsEvent.PlayingChanged(null),
                GlyphDetailsEvent.PlayingChanged(AudioAssets.letter(0)),
                GlyphDetailsEvent.PlayingChanged(null),
            ),
            scope.events.filterIsInstance<GlyphDetailsEvent.PlayingChanged>(),
        )
    }

    @Test
    fun `stop sound stops the player`() {
        actor().stopSound()

        assertEquals(1, player.stops)
    }

    @Test
    fun `select font emits event`() = runTest {
        val scope = RecordingScope(GlyphDetailsState(0), this)

        actor().execute(scope, GlyphDetailsAction.SelectFont("dejavu_sans"))

        assertEquals(listOf<GlyphDetailsEvent>(GlyphDetailsEvent.FontSelected("dejavu_sans")), scope.events)
    }

    // endregion

    // region Reducer

    @Test
    fun `reducer stores loaded letter and indexes related letters`() {
        val result = loaded()

        assertTrue(result.isLoaded)
        assertEquals(ANI, result.letter)
        assertEquals(ANI_DETAILS, result.details)
        assertEquals(mapOf(17 to SANI, 9 to KANI), result.relatedLetters)
    }

    @Test
    fun `reducer stores font and playing sound`() {
        val result = listOf(
            GlyphDetailsEvent.FontSelected("k1fs"),
            GlyphDetailsEvent.PlayingChanged("audio/letters/0.mp3"),
        ).fold(GlyphDetailsState(0), reducer::reduce)

        assertEquals("k1fs", result.currentFontId)
        assertEquals("audio/letters/0.mp3", result.playingId)
        assertNull(reducer.reduce(result, GlyphDetailsEvent.PlayingChanged(null)).playingId)
    }

    // endregion

    // region Mapper

    @Test
    fun `mapper is empty while loading`() {
        val ui = mapper(GlyphDetailsState(0))

        assertEquals(GlyphDetailsUiState(letterId = 0), ui)
        assertFalse(ui.isNotFound)
    }

    @Test
    fun `mapper marks unknown id as not found`() {
        val ui = mapper(loaded(letter = null, letterDetails = null))

        assertTrue(ui.isNotFound)
        assertEquals("", ui.symbol)
        assertEquals(99, ui.letterId)
    }

    @Test
    fun `mapper uses cyrillic for cyrillic readers`() {
        val ui = mapper(loaded(script = KnownScript.CYRILLIC))

        assertEquals("ა", ui.symbol)
        assertEquals("ka0", ui.header.nameGeorgian)
        assertEquals("имя0", ui.header.name)
        assertEquals("з0", ui.header.sound)
        assertEquals("ipa0", ui.header.ipa)
        assertEquals(KnownScript.CYRILLIC, ui.header.knownScript)
        assertEquals("ара", ui.words.single().transliteration)
        assertEquals(listOf(SimilarLetterUi(17, "ს", "имя17")), ui.similar.byShape)
        // Unknown id 99 has no chip
        assertEquals(listOf(SimilarLetterUi(9, "კ", "имя9")), ui.similar.bySound)
    }

    @Test
    fun `mapper uses latin for latin readers`() {
        val ui = mapper(loaded(script = KnownScript.LATIN))

        assertEquals("name0", ui.header.name)
        assertEquals("s0", ui.header.sound)
        assertEquals(KnownScript.LATIN, ui.header.knownScript)
        assertEquals("ara", ui.words.single().transliteration)
        assertEquals(listOf(SimilarLetterUi(17, "ს", "name17")), ui.similar.byShape)
    }

    @Test
    fun `mapper highlights the letter in example words`() {
        val word = mapper(loaded()).words.single()

        assertEquals("არა", word.georgian)
        assertEquals(listOf(WordPart("ა", true), WordPart("რ", false), WordPart("ა", true)), word.parts)
        assertEquals(LocalizedText("нет", "no"), word.translation)
        assertEquals(
            listOf(WordPart("ხ", false), WordPart("აა", true), WordPart("ჭპ", false)),
            "ხააჭპ".splitByLetter('ა'),
        )
    }

    @Test
    fun `mapper hides association and words of archaic letter`() {
        val archaicDetails = LetterDetails(33, null, emptyList(), emptyList(), emptyList())
        val ui = mapper(loaded(letter = ARCHAIC, letterDetails = archaicDetails))

        assertTrue(ui.header.archaic)
        assertNull(ui.association)
        assertTrue(ui.words.isEmpty())
        assertTrue(ui.similar.byShape.isEmpty())
        assertTrue(ui.similar.bySound.isEmpty())
    }

    @Test
    fun `mapper shows open fonts on stage and locked count`() {
        val open = FontCatalog.unlocked()
        val second = open[1]

        val stage = mapper(reducer.reduce(loaded(), GlyphDetailsEvent.FontSelected(second.id))).stage

        assertEquals(open, stage.fonts)
        assertEquals(1, stage.currentFontIndex)
        assertEquals(FontCatalog.all.size - open.size, stage.lockedFontsCount)
    }

    @Test
    fun `mapper falls back to first font for locked or unknown font`() {
        val locked = FontCatalog.all.first { !it.unlockedByDefault }

        assertEquals(0, mapper(loaded()).stage.currentFontIndex)
        assertEquals(0, mapper(loaded().copy(currentFontId = locked.id)).stage.currentFontIndex)
        assertEquals(0, mapper(loaded().copy(currentFontId = "missing")).stage.currentFontIndex)
    }

    @Test
    fun `mapper counts recognized fonts and accuracy`() {
        val open = FontCatalog.unlocked()
        val locked = FontCatalog.all.first { !it.unlockedByDefault }
        val progress = LetterProgress(
            letterId = 0,
            correct = 2,
            total = 3,
            recognizedFontIds = setOf(open[0].id, locked.id, "removed_font"),
            level = LearningLevel.FAMILIAR,
        )

        val mastery = mapper(loaded(progress = progress)).mastery

        assertEquals(LearningLevel.FAMILIAR, mastery.level)
        assertEquals(2, mastery.recognizedCount)
        assertEquals(FontCatalog.all.size, mastery.fontsCount)
        assertEquals(67, mastery.accuracyPercent)
        val statuses = mastery.cells.associate { it.font.id to it.status }
        assertEquals(FontCatalog.all.map { it.id }, mastery.cells.map { it.font.id })
        assertEquals(FontCellStatus.RECOGNIZED, statuses[open[0].id])
        assertEquals(FontCellStatus.LEARNING, statuses[open[1].id])
        // Recognized before the font was locked again still counts
        assertEquals(FontCellStatus.RECOGNIZED, statuses[locked.id])
        assertEquals(FontCellStatus.LOCKED, statuses[FontCatalog.all.last().id])
    }

    @Test
    fun `mapper shows new letter without accuracy`() {
        val mastery = mapper(loaded(progress = null)).mastery

        assertEquals(LearningLevel.NEW, mastery.level)
        assertEquals(0, mastery.recognizedCount)
        assertNull(mastery.accuracyPercent)
    }

    @Test
    fun `mapper marks playing name and word`() {
        val name = mapper(reducer.reduce(loaded(), GlyphDetailsEvent.PlayingChanged(AudioAssets.letter(0))))
        val word = mapper(reducer.reduce(loaded(), GlyphDetailsEvent.PlayingChanged("audio/words/ara.mp3")))

        assertEquals(AudioAssets.letter(0), name.header.nameAudio)
        assertTrue(name.header.isNamePlaying)
        assertFalse(name.words.single().isPlaying)
        assertFalse(word.header.isNamePlaying)
        assertTrue(word.words.single().isPlaying)
    }

    // endregion

    @Test
    fun `font survives process death, nav key font is the fallback`() {
        val handle = SavedStateHandle()

        assertEquals("k1fs", handle.restoreFontId(navFontId = "k1fs"))
        assertNull(handle.restoreFontId(navFontId = null))

        handle.saveFontId("xi20")

        assertEquals("xi20", handle.restoreFontId(navFontId = "k1fs"))
    }
}
