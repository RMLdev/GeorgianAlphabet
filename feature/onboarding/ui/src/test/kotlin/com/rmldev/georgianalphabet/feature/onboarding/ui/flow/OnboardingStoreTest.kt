package com.rmldev.georgianalphabet.feature.onboarding.ui.flow

import androidx.lifecycle.SavedStateHandle
import com.rmldev.georgianalphabet.core.ui.ActorScope
import com.rmldev.georgianalphabet.feature.onboarding.api.model.TransliteratedFragment
import com.rmldev.georgianalphabet.feature.onboarding.api.usecase.TransliterateNameUseCase
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.OnboardingActor
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.OnboardingReducer
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.message.OnboardingAction
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.message.OnboardingEvent
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.message.OnboardingSideEffect
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.OnboardingState
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.OnboardingStateToUiMapper
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.OnboardingStep
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.restoreOnboardingState
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.saveOnboardingState
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript
import com.rmldev.georgianalphabet.feature.preferences.api.model.UserPreferences
import com.rmldev.georgianalphabet.feature.preferences.api.usecase.CompleteOnboardingUseCase
import com.rmldev.georgianalphabet.feature.preferences.api.usecase.ObserveUserPreferencesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

private class FakeCompleteOnboarding : CompleteOnboardingUseCase {
    val calls = mutableListOf<Pair<KnownScript?, String?>>()
    override suspend fun invoke(knownScript: KnownScript?, userName: String?) {
        calls += knownScript to userName
    }
}

private class FakeObservePreferences(private val localeDefault: KnownScript) : ObserveUserPreferencesUseCase {
    override fun invoke(): Flow<UserPreferences> =
        flowOf(UserPreferences(onboardingCompleted = false, knownScript = localeDefault, userName = null))
}

// Буква в букву, как настоящий транслитератор для простых имён
private class FakeTransliterate : TransliterateNameUseCase {
    override fun invoke(text: String): List<TransliteratedFragment> =
        text.map { TransliteratedFragment(it.toString(), if (it == ' ') " " else "ა") }
}

private class RecordingScope(override var state: OnboardingState, override val coroutineScope: CoroutineScope) :
    ActorScope<OnboardingState, OnboardingEvent, OnboardingSideEffect> {
    val sideEffects = mutableListOf<OnboardingSideEffect>()
    override fun event(event: OnboardingEvent) {
        state = OnboardingReducer().reduce(state, event)
    }
    override suspend fun sideEffect(effect: OnboardingSideEffect) {
        sideEffects += effect
    }
}

class OnboardingStoreTest {

    private val complete = FakeCompleteOnboarding()
    private val actor = OnboardingActor(
        observeUserPreferences = FakeObservePreferences(KnownScript.LATIN),
        transliterateName = FakeTransliterate(),
        completeOnboarding = complete,
    )

    private suspend fun RecordingScope.execute(vararg actions: OnboardingAction) = with(actor) {
        actions.forEach { executeAction(it) }
    }

    @Test
    fun `init preselects locale script but keeps a restored choice`() = runTest {
        val fresh = RecordingScope(OnboardingState(), this)
        fresh.execute(OnboardingAction.Init)
        assertEquals(KnownScript.LATIN, fresh.state.knownScript)

        val restored = RecordingScope(OnboardingState(knownScript = KnownScript.CYRILLIC, userName = "Ан"), this)
        restored.execute(OnboardingAction.Init)
        assertEquals(KnownScript.CYRILLIC, restored.state.knownScript)
        assertEquals(2, restored.state.nameFragments.size)
    }

    @Test
    fun `steps go forward and back within bounds`() = runTest {
        val scope = RecordingScope(OnboardingState(), this)

        scope.execute(OnboardingAction.GoBack)
        assertEquals(OnboardingStep.WELCOME, scope.state.step)

        scope.execute(OnboardingAction.GoForward, OnboardingAction.GoForward, OnboardingAction.GoForward)
        assertEquals(OnboardingStep.NAME, scope.state.step)

        scope.execute(OnboardingAction.GoBack)
        assertEquals(OnboardingStep.SCRIPT, scope.state.step)
    }

    @Test
    fun `finish saves chosen script and name once`() = runTest {
        val scope = RecordingScope(OnboardingState(), this)

        scope.execute(
            OnboardingAction.SelectScript(KnownScript.CYRILLIC),
            OnboardingAction.ChangeName("Роман"),
            OnboardingAction.Finish(saveName = true),
            OnboardingAction.Finish(saveName = true),
        )

        assertEquals(listOf<Pair<KnownScript?, String?>>(KnownScript.CYRILLIC to "Роман"), complete.calls)
        assertEquals(listOf<OnboardingSideEffect>(OnboardingSideEffect.Finished), scope.sideEffects)
        assertEquals(true, scope.state.completing)
    }

    @Test
    fun `skip name keeps script, skip all keeps defaults`() = runTest {
        RecordingScope(OnboardingState(knownScript = KnownScript.LATIN, userName = "Anna"), this)
            .execute(OnboardingAction.Finish(saveName = false))
        RecordingScope(OnboardingState(knownScript = KnownScript.LATIN, userName = "Anna"), this)
            .execute(OnboardingAction.SkipAll)

        assertEquals(listOf<Pair<KnownScript?, String?>>(KnownScript.LATIN to null, null to null), complete.calls)
    }

    @Test
    fun `saved state survives process death`() {
        val handle = SavedStateHandle()
        val state = OnboardingState(
            step = OnboardingStep.NAME,
            knownScript = KnownScript.LATIN,
            userName = "Tamar",
            nameFragments = listOf(TransliteratedFragment("T", "ტ")),
            completing = true,
        )

        handle.saveOnboardingState(state)

        assertEquals(
            OnboardingState(step = OnboardingStep.NAME, knownScript = KnownScript.LATIN, userName = "Tamar"),
            handle.restoreOnboardingState(),
        )
        assertEquals(OnboardingState(), SavedStateHandle().restoreOnboardingState())
    }

    @Test
    fun `name step ui skips separators and counts unique letters`() {
        val state = OnboardingState(
            userName = "Анна Ия",
            nameFragments = listOf(
                TransliteratedFragment("А", "ა"),
                TransliteratedFragment("н", "ნ"),
                TransliteratedFragment("н", "ნ"),
                TransliteratedFragment("а", "ა"),
                TransliteratedFragment(" ", " "),
                TransliteratedFragment("И", "ი"),
                TransliteratedFragment("я", "ა"),
            ),
        )

        val name = OnboardingStateToUiMapper()(state).name

        assertEquals("ანნა ია", name.georgianName)
        assertEquals(6, name.fragments.size)
        assertEquals(3, name.uniqueLetterCount)
        assertEquals("", OnboardingStateToUiMapper()(OnboardingState(userName = "1")).name.georgianName)
    }
}
