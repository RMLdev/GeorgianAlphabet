package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.rmldev.georgianalphabet.core.ui.StoreViewModel
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.OnboardingActor
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.OnboardingReducer
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.message.OnboardingAction
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.message.OnboardingEvent
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.message.OnboardingIntent
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.message.OnboardingSideEffect
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.OnboardingState
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.OnboardingStateToUiMapper
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.OnboardingUiState
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.restoreOnboardingState
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.saveOnboardingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Один стор на все шаги онбординга: шаг, алфавит и имя переживают поворот и смерть процесса. */
@HiltViewModel
internal class OnboardingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
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
    initialState = savedStateHandle.restoreOnboardingState(),
    uiStateMapper = uiStateMapper,
    initialActions = listOf(OnboardingAction.Init),
    intentToAction = { it.toAction() },
) {

    init {
        viewModelScope.launch {
            state.collect { savedStateHandle.saveOnboardingState(it) }
        }
    }
}

private fun OnboardingIntent.toAction(): OnboardingAction = when (this) {
    OnboardingIntent.BackClicked -> OnboardingAction.GoBack
    OnboardingIntent.StartClicked, OnboardingIntent.NextClicked -> OnboardingAction.GoForward
    OnboardingIntent.SkipClicked -> OnboardingAction.SkipAll
    is OnboardingIntent.ScriptSelected -> OnboardingAction.SelectScript(script)
    is OnboardingIntent.NameChanged -> OnboardingAction.ChangeName(name)
    OnboardingIntent.SkipNameClicked -> OnboardingAction.Finish(saveName = false)
    OnboardingIntent.FinishClicked -> OnboardingAction.Finish(saveName = true)
}
