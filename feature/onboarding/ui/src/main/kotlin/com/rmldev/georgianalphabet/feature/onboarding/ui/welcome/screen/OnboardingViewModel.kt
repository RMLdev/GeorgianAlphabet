package com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen

import com.rmldev.georgianalphabet.core.ui.StoreViewModel
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.OnboardingActor
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.OnboardingReducer
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.message.OnboardingAction
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.message.OnboardingEvent
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.message.OnboardingIntent
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.message.OnboardingSideEffect
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.state.OnboardingState
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.state.OnboardingStateToUiMapper
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.state.OnboardingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class OnboardingViewModel @Inject constructor(
    actor: OnboardingActor,
    reducer: OnboardingReducer,
    uiStateMapper: OnboardingStateToUiMapper,
) : StoreViewModel<
    OnboardingIntent,
    OnboardingAction,
    OnboardingState,
    OnboardingUiState,
    OnboardingSideEffect,
    OnboardingEvent,
    >(
    actor = actor,
    reducer = reducer,
    initialState = OnboardingState(),
    uiStateMapper = uiStateMapper,
    intentToAction = { intent ->
        when (intent) {
            OnboardingIntent.StartClicked, OnboardingIntent.SkipClicked -> OnboardingAction.Complete
        }
    },
)
