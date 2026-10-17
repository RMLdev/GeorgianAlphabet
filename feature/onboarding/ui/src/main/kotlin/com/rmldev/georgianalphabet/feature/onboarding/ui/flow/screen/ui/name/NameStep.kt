package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.ui.name

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.item.GeAlphOutlinedTextField
import com.rmldev.georgianalphabet.core.designsystem.theme.NotoSerifGeorgian
import com.rmldev.georgianalphabet.feature.onboarding.ui.R
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.message.OnboardingIntent
import com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state.NameStepUiState
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript

/**
 * Шаг 3, имя по-грузински (макеты Onb3, Onb4). Рисуется внутри каркаса шагов:
 * шапка с «Пропустить» и кнопка «Начать учить» уже есть, здесь только содержимое.
 * Заготовка: блок с именем мхедрули, чипы разбора, счётчик букв и IME Done делает задача 10.
 */
@Composable
internal fun NameStep(state: NameStepUiState, onIntent: (OnboardingIntent) -> Unit, modifier: Modifier = Modifier) {
    val fieldState = rememberTextFieldState(initialText = state.name)
    val currentOnIntent by rememberUpdatedState(onIntent)
    LaunchedEffect(fieldState) {
        snapshotFlow { fieldState.text.toString() }
            .collect { currentOnIntent(OnboardingIntent.NameChanged(it)) }
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier) {
        Text(
            text = stringResource(R.string.feature_onboarding_ui_name_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = stringResource(R.string.feature_onboarding_ui_name_body),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        GeAlphOutlinedTextField(
            state = fieldState,
            labelText = stringResource(R.string.feature_onboarding_ui_name_label),
            placeholderText = stringResource(
                if (state.knownScript == KnownScript.LATIN) {
                    R.string.feature_onboarding_ui_name_placeholder_latin
                } else {
                    R.string.feature_onboarding_ui_name_placeholder_cyrillic
                },
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        if (state.georgianName.isNotEmpty()) {
            Text(
                text = state.georgianName,
                style = MaterialTheme.typography.headlineLarge.copy(fontFamily = NotoSerifGeorgian),
            )
        }
    }
}
