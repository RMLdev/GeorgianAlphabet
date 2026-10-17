package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.font.FontFamily
import com.rmldev.georgianalphabet.core.designsystem.font.FontCatalog
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/** Шаг «Приветствие» (задача 8). */
@Stable
internal data class WelcomeStepUiState(
    val buttonsEnabled: Boolean,
    /** Шрифты мозаики: открытые на старте, первый — стандартный Noto Serif Georgian. */
    val mosaicFonts: ImmutableList<FontFamily>,
)

internal fun OnboardingState.toWelcomeStepUi(): WelcomeStepUiState = WelcomeStepUiState(
    buttonsEnabled = !completing,
    // На онбординге наградой ещё ничего не открыто, поэтому только стартовые шрифты
    mosaicFonts = FontCatalog.unlocked().map { it.fontFamily }.toImmutableList(),
)
