package com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store

import com.rmldev.georgianalphabet.core.ui.MviReducer
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.message.OnboardingEvent
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.state.OnboardingState
import javax.inject.Inject

internal class OnboardingReducer @Inject constructor() : MviReducer<OnboardingState, OnboardingEvent> {

    override fun reduce(oldState: OnboardingState, event: OnboardingEvent): OnboardingState = when (event) {
        OnboardingEvent.CompletionStarted -> oldState.copy(completing = true)
    }
}
