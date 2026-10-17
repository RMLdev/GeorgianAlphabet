package com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme
import com.rmldev.georgianalphabet.feature.onboarding.ui.R
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.message.OnboardingIntent
import com.rmldev.georgianalphabet.feature.onboarding.ui.welcome.screen.store.state.OnboardingUiState

/** Временный экран: настоящие шаги онбординга появятся в задачах 7–10. */
@Composable
internal fun OnboardingScreen(state: OnboardingUiState, onIntent: (OnboardingIntent) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(24.dp),
    ) {
        Text(
            text = stringResource(R.string.feature_onboarding_ui_welcome_title),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = { onIntent(OnboardingIntent.StartClicked) },
            enabled = state.buttonsEnabled,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.feature_onboarding_ui_start))
        }
        TextButton(
            onClick = { onIntent(OnboardingIntent.SkipClicked) },
            enabled = state.buttonsEnabled,
        ) {
            Text(stringResource(R.string.feature_onboarding_ui_skip))
        }
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    GeAlphTheme {
        OnboardingScreen(state = OnboardingUiState(buttonsEnabled = true), onIntent = {})
    }
}
