package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.ui.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.feature.onboarding.ui.R
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.message.OnboardingIntent
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.WelcomeStepUiState

/**
 * Шаг 1, приветствие (макет Main). Занимает весь экран, без каркаса шагов.
 * Заготовка: мозаику из плиток с буквой ა и вёрстку по макету делает задача 8.
 */
@Composable
internal fun WelcomeStep(
    state: WelcomeStepUiState,
    onIntent: (OnboardingIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(start = 24.dp, end = 24.dp, top = 64.dp, bottom = 28.dp),
    ) {
        Text(
            text = stringResource(R.string.feature_onboarding_ui_welcome_title),
            style = MaterialTheme.typography.headlineLarge,
        )
        Text(
            text = stringResource(R.string.feature_onboarding_ui_welcome_body),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.weight(1f))
        Button(
            onClick = { onIntent(OnboardingIntent.StartClicked) },
            enabled = state.buttonsEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        ) {
            Text(stringResource(R.string.feature_onboarding_ui_welcome_start))
        }
        TextButton(
            onClick = { onIntent(OnboardingIntent.SkipClicked) },
            enabled = state.buttonsEnabled,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Text(stringResource(R.string.feature_onboarding_ui_welcome_skip))
        }
    }
}
