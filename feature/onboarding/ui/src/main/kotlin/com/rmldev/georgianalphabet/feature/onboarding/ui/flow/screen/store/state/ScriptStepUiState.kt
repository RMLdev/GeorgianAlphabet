package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state

import androidx.compose.runtime.Stable
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/** Шаг «Какой алфавит тебе ближе?» (задача 9). */
@Stable
internal data class ScriptStepUiState(
    /** Карточки в порядке [KnownScript.entries]. */
    val options: ImmutableList<ScriptOptionUi>,
)

/** Карточка-радиокнопка одного алфавита. */
@Stable
internal data class ScriptOptionUi(
    val script: KnownScript,
    /** До чтения значения по локали не выбрана ни одна карточка. */
    val selected: Boolean,
    /** Плитки с узнаваемыми буквами алфавита: «Бб», «Жж», «Яя». */
    val glyphs: ImmutableList<String>,
    /** Как на этом алфавите подписано название буквы ანი. */
    val example: String,
)

/** Буквы на плитках и пример подписи не переводятся: это образцы самих алфавитов. */
private fun KnownScript.glyphs(): ImmutableList<String> = when (this) {
    KnownScript.CYRILLIC -> persistentListOf("Бб", "Жж", "Яя")
    KnownScript.LATIN -> persistentListOf("Gg", "Qq", "Ww")
}

private fun KnownScript.example(): String = when (this) {
    KnownScript.CYRILLIC -> "ани"
    KnownScript.LATIN -> "ani"
}

internal fun OnboardingState.toScriptStepUi(): ScriptStepUiState = ScriptStepUiState(
    options = KnownScript.entries
        .map { script ->
            ScriptOptionUi(
                script = script,
                selected = script == knownScript,
                glyphs = script.glyphs(),
                example = script.example(),
            )
        }
        .toImmutableList(),
)
