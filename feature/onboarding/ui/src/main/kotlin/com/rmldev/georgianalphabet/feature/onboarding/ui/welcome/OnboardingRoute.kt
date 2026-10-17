package com.rmldev.georgianalphabet.feature.onboarding.ui.welcome

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rmldev.georgianalphabet.core.ui.CollectSideEffects
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.OnboardingViewModel
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.message.OnboardingSideEffect
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.ui.OnboardingScreen

@Composable
internal fun OnboardingRoute(onFinished: () -> Unit, viewModel: OnboardingViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CollectSideEffects(viewModel.sideEffects) { sideEffect ->
        when (sideEffect) {
            OnboardingSideEffect.Finished -> onFinished()
        }
    }

    OnboardingScreen(
        state = state,
        onIntent = viewModel::dispatch,
    )
}
