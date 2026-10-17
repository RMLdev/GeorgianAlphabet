package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store

import com.rmldev.georgianalphabet.core.ui.MviReducer
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.message.OnboardingEvent
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.OnboardingState
import javax.inject.Inject

internal class OnboardingReducer @Inject constructor() : MviReducer<OnboardingState, OnboardingEvent> {

    override fun reduce(oldState: OnboardingState, event: OnboardingEvent): OnboardingState = when (event) {
        is OnboardingEvent.StepChanged -> oldState.copy(step = event.step)
        is OnboardingEvent.ScriptSelected -> oldState.copy(knownScript = event.script)
        is OnboardingEvent.NameChanged -> oldState.copy(userName = event.name, nameFragments = event.fragments)
        OnboardingEvent.CompletionStarted -> oldState.copy(completing = true)
    }
}
