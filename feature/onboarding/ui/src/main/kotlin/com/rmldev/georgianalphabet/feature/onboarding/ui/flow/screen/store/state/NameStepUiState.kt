package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state

import androidx.compose.runtime.Stable
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/** Шаг «Как тебя зовут?» (задача 10). */
@Stable
internal data class NameStepUiState(
    val name: String,
    /** Для подсказки в поле на знакомом алфавите: «Анна» или «Anna». */
    val knownScript: KnownScript?,
    /** Имя мхедрули с пробелами и дефисами; пустое, пока нечего показать. */
    val georgianName: String,
    /** Чипы разбора «Р → რ», без пробелов и дефисов. */
    val fragments: ImmutableList<NameFragmentUi>,
    /** Сколько разных букв алфавита в имени. */
    val uniqueLetterCount: Int,
)

@Stable
internal data class NameFragmentUi(val source: String, val georgian: String)

private val SEPARATORS = setOf(" ", "-")

internal fun OnboardingState.toNameStepUi(): NameStepUiState {
    val letters = nameFragments.filterNot { it.source in SEPARATORS }
    return NameStepUiState(
        name = userName,
        knownScript = knownScript,
        georgianName = if (letters.isEmpty()) "" else nameFragments.joinToString("") { it.georgian }.trim(),
        fragments = letters.map { NameFragmentUi(source = it.source, georgian = it.georgian) }.toImmutableList(),
        uniqueLetterCount = letters.flatMap { it.georgian.toList() }.toSet().size,
    )
}
