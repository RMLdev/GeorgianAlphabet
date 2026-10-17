package com.rmldev.georgianalphabet.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Colors the app needs beyond the Material 3 [androidx.compose.material3.ColorScheme].
 * They stay fixed when dynamic color is on, so mastery levels and answer feedback look
 * the same on any palette. Never use color alone: pair it with segments, an icon or text.
 *
 * @param masteryEmpty Unfilled segment of the mastery indicator (a new letter).
 * @param masteryFamiliar Filled segments of a familiar letter (1 of 3).
 * @param masteryConfident Filled segments of a confident letter (2 of 3); also the
 *        "recognized in this font" mark on the letter card and in "All fonts".
 * @param masteryConfidentContainer Background of a font cell where the letter is recognized.
 * @param onMasteryConfident Icon on [masteryConfident], like the check badge.
 * @param masteryMaster Filled segments and star of a mastered letter (3 of 3).
 * @param correct Icon and text of a correct answer.
 * @param correctContainer Background of the correct answer option and the "Верно!" panel.
 * @param onCorrectContainer Text on [correctContainer].
 * @param wrong Icon and text of a wrong answer.
 * @param wrongContainer Background of the wrong answer option and the "Ошибка" panel.
 * @param onWrongContainer Text on [wrongContainer].
 */
@Immutable
data class LearningColors(
    val masteryEmpty: Color = Color.Unspecified,
    val masteryFamiliar: Color = Color.Unspecified,
    val masteryConfident: Color = Color.Unspecified,
    val masteryConfidentContainer: Color = Color.Unspecified,
    val onMasteryConfident: Color = Color.Unspecified,
    val masteryMaster: Color = Color.Unspecified,
    val correct: Color = Color.Unspecified,
    val correctContainer: Color = Color.Unspecified,
    val onCorrectContainer: Color = Color.Unspecified,
    val wrong: Color = Color.Unspecified,
    val wrongContainer: Color = Color.Unspecified,
    val onWrongContainer: Color = Color.Unspecified,
) {
    /**
     * Segment color for a mastery [level]: 0 new, 1 familiar, 2 confident, 3 master.
     */
    fun mastery(level: Int): Color = when (level) {
        1 -> masteryFamiliar
        2 -> masteryConfident
        3 -> masteryMaster
        else -> masteryEmpty
    }
}

/**
 * Light learning colors
 */
val LightLearningColors = LearningColors(
    masteryEmpty = Neutral88,
    masteryFamiliar = Amber40,
    masteryConfident = Blue40,
    masteryConfidentContainer = Blue92,
    onMasteryConfident = Color.White,
    masteryMaster = Green40,
    correct = Green40,
    correctContainer = Green92,
    onCorrectContainer = Green20,
    wrong = Red40,
    wrongContainer = Red90,
    onWrongContainer = Red10,
)

/**
 * Dark learning colors
 */
val DarkLearningColors = LearningColors(
    masteryEmpty = Neutral26,
    masteryFamiliar = Amber80,
    masteryConfident = Blue80,
    masteryConfidentContainer = Blue25,
    onMasteryConfident = Blue20,
    masteryMaster = Green80,
    correct = Green80,
    correctContainer = Green25,
    onCorrectContainer = Green90,
    wrong = Red80,
    wrongContainer = Red30,
    onWrongContainer = Red90,
)

/**
 * A composition local for [LearningColors].
 */
val LocalLearningColors = staticCompositionLocalOf { LearningColors() }
