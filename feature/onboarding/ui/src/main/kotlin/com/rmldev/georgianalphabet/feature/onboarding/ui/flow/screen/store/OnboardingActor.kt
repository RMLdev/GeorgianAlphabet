package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store

import com.rmldev.georgianalphabet.core.ui.ActorScope
import com.rmldev.georgianalphabet.core.ui.MviActor
import com.rmldev.georgianalphabet.feature.onboarding.api.usecase.TransliterateNameUseCase
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.message.OnboardingAction
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.message.OnboardingEvent
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.message.OnboardingSideEffect
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.OnboardingState
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript
import com.rmldev.georgianalphabet.feature.preferences.api.usecase.CompleteOnboardingUseCase
import com.rmldev.georgianalphabet.feature.preferences.api.usecase.ObserveUserPreferencesUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private typealias OnboardingActorScope = ActorScope<OnboardingState, OnboardingEvent, OnboardingSideEffect>

internal class OnboardingActor @Inject constructor(
    private val observeUserPreferences: ObserveUserPreferencesUseCase,
    private val transliterateName: TransliterateNameUseCase,
    private val completeOnboarding: CompleteOnboardingUseCase,
) : MviActor<OnboardingAction, OnboardingState, OnboardingEvent, OnboardingSideEffect> {

    override suspend fun OnboardingActorScope.executeAction(action: OnboardingAction) {
        when (action) {
            OnboardingAction.Init -> init()

            OnboardingAction.GoForward -> state.step.next?.let { event(OnboardingEvent.StepChanged(it)) }

            OnboardingAction.GoBack -> state.step.previous?.let { event(OnboardingEvent.StepChanged(it)) }

            is OnboardingAction.SelectScript -> event(OnboardingEvent.ScriptSelected(action.script))

            is OnboardingAction.ChangeName -> changeName(action.name)

            OnboardingAction.SkipAll -> complete(knownScript = null, userName = null)

            is OnboardingAction.Finish -> complete(
                knownScript = state.knownScript,
                userName = state.userName.takeIf { action.saveName },
            )
        }
    }

    private suspend fun OnboardingActorScope.init() {
        if (state.userName.isNotEmpty()) changeName(state.userName)
        if (state.knownScript == null) {
            val localeDefault = observeUserPreferences().first().knownScript
            // Пользователь мог успеть выбрать сам, пока читались настройки
            if (state.knownScript == null) event(OnboardingEvent.ScriptSelected(localeDefault))
        }
    }

    private fun OnboardingActorScope.changeName(name: String) {
        event(OnboardingEvent.NameChanged(name = name, fragments = transliterateName(name)))
    }

    private suspend fun OnboardingActorScope.complete(knownScript: KnownScript?, userName: String?) {
        if (state.completing) return
        event(OnboardingEvent.CompletionStarted)
        completeOnboarding(knownScript = knownScript, userName = userName)
        sideEffect(OnboardingSideEffect.Finished)
    }
}
