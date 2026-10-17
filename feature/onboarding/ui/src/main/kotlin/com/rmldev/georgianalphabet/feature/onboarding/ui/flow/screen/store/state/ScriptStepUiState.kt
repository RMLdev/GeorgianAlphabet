package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state

import androidx.compose.runtime.Stable
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript

/** Шаг «Какой алфавит тебе ближе?» (задача 9). */
@Stable
internal data class ScriptStepUiState(
    /** `null` до чтения значения по локали: ни одна карточка не выбрана. */
    val selected: KnownScript?,
)

internal fun OnboardingState.toScriptStepUi(): ScriptStepUiState = ScriptStepUiState(
    selected = knownScript,
)
