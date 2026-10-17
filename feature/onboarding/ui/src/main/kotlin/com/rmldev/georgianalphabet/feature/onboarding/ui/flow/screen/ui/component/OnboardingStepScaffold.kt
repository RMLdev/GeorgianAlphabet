package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.icon.GeAlphIcons
import com.rmldev.georgianalphabet.feature.onboarding.ui.R

/**
 * Общий каркас шагов 2–3 (макеты Onb2, Onb3): шапка со стрелкой «Назад», сегментами прогресса
 * и необязательным действием справа, прокручиваемое содержимое шага и нижняя кнопка.
 * Отступы учитывают системные панели и клавиатуру, поэтому кнопка не уходит под IME.
 */
@Composable
internal fun OnboardingStepScaffold(
    stepNumber: Int,
    stepsCount: Int,
    buttonText: String,
    buttonsEnabled: Boolean,
    onBackClick: () -> Unit,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    topAction: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(bottom = 28.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 8.dp, top = 16.dp)
                .height(48.dp),
        ) {
            IconButton(onClick = onBackClick, enabled = buttonsEnabled) {
                Icon(
                    imageVector = GeAlphIcons.ArrowBack,
                    contentDescription = stringResource(R.string.feature_onboarding_ui_back),
                )
            }
            StepProgressIndicator(
                current = stepNumber,
                total = stepsCount,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
            )
            Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.width(TopActionWidth)) {
                topAction?.invoke()
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, top = 32.dp, bottom = 24.dp),
        ) {
            content()
        }
        Button(
            onClick = onButtonClick,
            enabled = buttonsEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(56.dp),
        ) {
            Text(buttonText)
        }
    }
}

// Ширина как у макета: сегменты не сдвигаются, когда «Пропустить» появляется на шаге имени
private val TopActionWidth = 112.dp
