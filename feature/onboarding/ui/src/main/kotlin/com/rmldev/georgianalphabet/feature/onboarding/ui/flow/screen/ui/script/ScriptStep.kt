package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.ui.script

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.feature.onboarding.ui.R
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.message.OnboardingIntent
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.ScriptStepUiState
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript

/**
 * Шаг 2, выбор знакомого алфавита (макет Onb2). Рисуется внутри каркаса шагов:
 * шапка и кнопка «Дальше» уже есть, здесь только содержимое.
 * Заготовка: карточки с плитками Бб Жж Яя / Gg Qq Ww делает задача 9.
 */
@Composable
internal fun ScriptStep(
    state: ScriptStepUiState,
    onIntent: (OnboardingIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier) {
        Text(
            text = stringResource(R.string.feature_onboarding_ui_script_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = stringResource(R.string.feature_onboarding_ui_script_body),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(modifier = Modifier.selectableGroup()) {
            KnownScript.entries.forEach { script ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = script == state.selected,
                            role = Role.RadioButton,
                            onClick = { onIntent(OnboardingIntent.ScriptSelected(script)) },
                        )
                        .padding(vertical = 12.dp),
                ) {
                    RadioButton(selected = script == state.selected, onClick = null)
                    Text(
                        text = stringResource(script.labelRes),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 12.dp),
                    )
                }
            }
        }
    }
}

private val KnownScript.labelRes: Int
    get() = when (this) {
        KnownScript.CYRILLIC -> R.string.feature_onboarding_ui_script_cyrillic
        KnownScript.LATIN -> R.string.feature_onboarding_ui_script_latin
    }
