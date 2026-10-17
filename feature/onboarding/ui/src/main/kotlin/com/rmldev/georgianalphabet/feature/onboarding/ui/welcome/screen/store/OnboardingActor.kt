package com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store

import com.rmldev.georgianalphabet.core.ui.ActorScope
import com.rmldev.georgianalphabet.core.ui.MviActor
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.message.OnboardingAction
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.message.OnboardingEvent
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.message.OnboardingSideEffect
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.state.OnboardingState
import com.rmldev.georgianalphabet.feature.preferences.api.usecase.CompleteOnboardingUseCase
import javax.inject.Inject

private typealias OnboardingActorScope = ActorScope<OnboardingState, OnboardingEvent, OnboardingSideEffect>

internal class OnboardingActor @Inject constructor(private val completeOnboarding: CompleteOnboardingUseCase) :
    MviActor<OnboardingAction, OnboardingState, OnboardingEvent, OnboardingSideEffect> {

    override suspend fun OnboardingActorScope.executeAction(action: OnboardingAction) {
        when (action) {
            OnboardingAction.Complete -> {
                if (state.completing) return
                event(OnboardingEvent.CompletionStarted)
                // Шаги выбора алфавита и имени появятся в задачах 9 и 10, пока — значения по умолчанию
                completeOnboarding(knownScript = null, userName = null)
                sideEffect(OnboardingSideEffect.Finished)
            }
        }
    }
}
