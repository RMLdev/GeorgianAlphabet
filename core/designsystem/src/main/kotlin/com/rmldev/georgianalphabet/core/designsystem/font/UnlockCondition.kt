package com.rmldev.georgianalphabet.core.designsystem.font

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.R
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme

/**
 * За что закрытый шрифт открывается наградой (concept.md 2.3). Проверка условий и выдача награды —
 * в коллекции шрифтов, здесь только описание.
 */
@Immutable
sealed interface UnlockCondition {

    /** Пройден урок [number] (1–6, карта уроков concept.md 4). */
    data class Lesson(val number: Int) : UnlockCondition

    /** Серия занятий [days] дней подряд. */
    data class Streak(val days: Int) : UnlockCondition

    /** Освоено [count] букв. */
    data class LettersMastered(val count: Int) : UnlockCondition

    /** Пройдены все уроки. */
    data object AllLessons : UnlockCondition
}

/** Короткая подпись условия для замочка: «Урок 3», «7 дней подряд», «10 букв», «Все уроки». */
@Composable
fun UnlockCondition.label(): String = when (this) {
    is UnlockCondition.Lesson -> stringResource(R.string.core_designsystem_unlock_lesson, number)

    is UnlockCondition.Streak -> pluralStringResource(R.plurals.core_designsystem_unlock_streak, days, days)

    is UnlockCondition.LettersMastered ->
        pluralStringResource(R.plurals.core_designsystem_unlock_letters_mastered, count, count)

    UnlockCondition.AllLessons -> stringResource(R.string.core_designsystem_unlock_all_lessons)
}

@Suppress("UnusedPrivateMember")
@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun UnlockConditionLabelPreview() {
    GeAlphTheme {
        Surface {
            Column(Modifier.padding(8.dp)) {
                FontCatalog.all.forEach { font ->
                    val condition = font.unlockCondition ?: return@forEach
                    Text("${font.displayName}: ${condition.label()}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
