package com.rmldev.georgianalphabet.feature.onboarding.ui.welcome

import com.rmldev.georgianalphabet.core.ui.ActorScope
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.OnboardingActor
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.OnboardingReducer
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.message.OnboardingAction
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.message.OnboardingEvent
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.message.OnboardingSideEffect
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.state.OnboardingState
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript
import com.rmldev.georgianalphabet.feature.preferences.api.usecase.CompleteOnboardingUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

private class FakeCompleteOnboarding : CompleteOnboardingUseCase {
    var calls = 0
    override suspend fun invoke(knownScript: KnownScript?, userName: String?) {
        calls++
    }
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

    @Test
    fun `complete saves preferences and finishes once`() = runTest {
        val useCase = FakeCompleteOnboarding()
        val actor = OnboardingActor(useCase)
        val scope = RecordingScope(OnboardingState(), this)

        with(actor) {
            scope.executeAction(OnboardingAction.Complete)
            scope.executeAction(OnboardingAction.Complete)
        }

        assertEquals(1, useCase.calls)
        assertEquals(listOf<OnboardingSideEffect>(OnboardingSideEffect.Finished), scope.sideEffects)
        assertEquals(true, scope.state.completing)
    }
}
