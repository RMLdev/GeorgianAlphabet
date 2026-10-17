package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme
import com.rmldev.georgianalphabet.feature.onboarding.api.model.TransliteratedFragment
import com.rmldev.georgianalphabet.feature.onboarding.ui.R
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.message.OnboardingIntent
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.OnboardingState
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.OnboardingStateToUiMapper
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.OnboardingStep
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.OnboardingUiState
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.ui.component.OnboardingStepScaffold
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.ui.name.NameStep
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.ui.script.ScriptStep
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.ui.welcome.WelcomeStep
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript

/**
 * Онбординг целиком: приветствие на весь экран, шаги 2–3 в общем каркасе.
 * Содержимое каждого шага живёт в своём пакете (welcome, script, name), чтобы шаги
 * можно было верстать независимо. Системный «Назад» на шагах 2–3 ведёт на предыдущий шаг.
 */
@Composable
internal fun OnboardingScreen(state: OnboardingUiState, onIntent: (OnboardingIntent) -> Unit) {
    BackHandler(enabled = state.step != OnboardingStep.WELCOME) { onIntent(OnboardingIntent.BackClicked) }

    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = state.step,
            transitionSpec = { stepTransition() },
            // Шаги 2–3 делят каркас: шапка и кнопка остаются, анимируется только содержимое
            contentKey = { it == OnboardingStep.WELCOME },
            label = "OnboardingWelcome",
        ) { step ->
            when (step) {
                OnboardingStep.WELCOME -> WelcomeStep(state = state.welcome, onIntent = onIntent)
                OnboardingStep.SCRIPT, OnboardingStep.NAME -> OnboardingSteps(step, state, onIntent)
            }
        }
    }
}

@Composable
private fun OnboardingSteps(step: OnboardingStep, state: OnboardingUiState, onIntent: (OnboardingIntent) -> Unit) {
    val isName = step == OnboardingStep.NAME
    OnboardingStepScaffold(
        stepNumber = step.ordinal,
        stepsCount = OnboardingStep.entries.size - 1,
        buttonText = stringResource(
            if (isName) R.string.feature_onboarding_ui_finish else R.string.feature_onboarding_ui_next,
        ),
        buttonsEnabled = state.buttonsEnabled,
        onBackClick = { onIntent(OnboardingIntent.BackClicked) },
        onButtonClick = { onIntent(if (isName) OnboardingIntent.FinishClicked else OnboardingIntent.NextClicked) },
        topAction = if (isName) {
            {
                TextButton(
                    onClick = { onIntent(OnboardingIntent.SkipNameClicked) },
                    enabled = state.buttonsEnabled,
                ) {
                    Text(stringResource(R.string.feature_onboarding_ui_skip))
                }
            }
        } else {
            null
        },
    ) {
        AnimatedContent(
            targetState = step,
            transitionSpec = { stepTransition() },
            label = "OnboardingStepContent",
        ) { contentStep ->
            when (contentStep) {
                OnboardingStep.SCRIPT -> ScriptStep(
                    state = state.script,
                    onIntent = onIntent,
                    modifier = Modifier.fillMaxWidth(),
                )

                OnboardingStep.NAME -> NameStep(
                    state = state.name,
                    onIntent = onIntent,
                    modifier = Modifier.fillMaxWidth(),
                )

                OnboardingStep.WELCOME -> Unit
            }
        }
    }
}

/** Вперёд — содержимое уезжает влево, назад — вправо. */
private fun AnimatedContentTransitionScope<OnboardingStep>.stepTransition(): ContentTransform {
    val forward = targetState.ordinal > initialState.ordinal
    return (slideInHorizontally { width -> if (forward) width else -width } + fadeIn())
        .togetherWith(slideOutHorizontally { width -> if (forward) -width else width } + fadeOut())
}

private fun previewState(step: OnboardingStep): OnboardingUiState = OnboardingStateToUiMapper()(
    OnboardingState(
        step = step,
        knownScript = KnownScript.CYRILLIC,
        userName = "Роман",
        nameFragments = listOf("Р" to "რ", "о" to "ო", "м" to "მ", "а" to "ა", "н" to "ნ")
            .map { (source, georgian) -> TransliteratedFragment(source, georgian) },
    ),
)

@Composable
private fun OnboardingScreenPreview(step: OnboardingStep, darkTheme: Boolean) {
    GeAlphTheme(darkTheme = darkTheme) {
        OnboardingScreen(state = previewState(step), onIntent = {})
    }
}

@Preview(name = "Welcome · light", widthDp = 390, heightDp = 844)
@Composable
private fun WelcomeLightPreview() = OnboardingScreenPreview(OnboardingStep.WELCOME, darkTheme = false)

@Preview(name = "Welcome · dark", widthDp = 390, heightDp = 844)
@Composable
private fun WelcomeDarkPreview() = OnboardingScreenPreview(OnboardingStep.WELCOME, darkTheme = true)

@Preview(name = "Script · light", widthDp = 390, heightDp = 844)
@Composable
private fun ScriptLightPreview() = OnboardingScreenPreview(OnboardingStep.SCRIPT, darkTheme = false)

@Preview(name = "Script · dark", widthDp = 390, heightDp = 844)
@Composable
private fun ScriptDarkPreview() = OnboardingScreenPreview(OnboardingStep.SCRIPT, darkTheme = true)

@Preview(name = "Name · light", widthDp = 390, heightDp = 844)
@Composable
private fun NameLightPreview() = OnboardingScreenPreview(OnboardingStep.NAME, darkTheme = false)

@Preview(name = "Name · dark", widthDp = 390, heightDp = 844)
@Composable
private fun NameDarkPreview() = OnboardingScreenPreview(OnboardingStep.NAME, darkTheme = true)
