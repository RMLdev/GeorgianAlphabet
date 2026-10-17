package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.feature.onboarding.ui.R

/** Сегменты прогресса шагов онбординга: пройденные и текущий закрашены primary. */
@Composable
internal fun StepProgressIndicator(current: Int, total: Int, modifier: Modifier = Modifier) {
    val description = stringResource(R.string.feature_onboarding_ui_step_progress, current, total)
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier.clearAndSetSemantics { contentDescription = description },
    ) {
        repeat(total) { index ->
            val color by animateColorAsState(
                targetValue = if (index < current) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surfaceContainerHighest
                },
                label = "StepSegmentColor",
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .background(color, RoundedCornerShape(3.dp)),
            )
        }
    }
}
