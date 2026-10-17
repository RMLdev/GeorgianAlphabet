package com.rmldev.georgianalphabet.core.designsystem.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * Light default theme color scheme
 */
@VisibleForTesting
val LightDefaultColorScheme = lightColorScheme(
    primary = Pomegranate40,
    onPrimary = Color.White,
    primaryContainer = Pomegranate90,
    onPrimaryContainer = Pomegranate10,
    inversePrimary = Pomegranate80,
    secondary = Rose40,
    onSecondary = Color.White,
    secondaryContainer = Rose90,
    onSecondaryContainer = Rose10,
    tertiary = Grape40,
    onTertiary = Color.White,
    tertiaryContainer = Grape90,
    onTertiaryContainer = Grape10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = Neutral98,
    onBackground = Neutral10,
    surface = Neutral98,
    onSurface = Neutral10,
    surfaceVariant = Neutral90,
    onSurfaceVariant = NeutralVariant30,
    surfaceTint = Pomegranate40,
    inverseSurface = Neutral20,
    inverseOnSurface = Neutral95,
    outline = NeutralVariant50,
    outlineVariant = NeutralVariant80,
    scrim = Color.Black,
    surfaceBright = Neutral98,
    surfaceDim = Neutral87,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Neutral96,
    surfaceContainer = Neutral94,
    surfaceContainerHigh = Neutral92,
    surfaceContainerHighest = Neutral90,
)

/**
 * Dark default theme color scheme
 */
@VisibleForTesting
val DarkDefaultColorScheme = darkColorScheme(
    primary = Pomegranate80,
    onPrimary = Pomegranate20,
    primaryContainer = Pomegranate30,
    onPrimaryContainer = Pomegranate90,
    inversePrimary = Pomegranate40,
    secondary = Rose80,
    onSecondary = Rose20,
    secondaryContainer = Rose30,
    onSecondaryContainer = Rose90,
    tertiary = Grape80,
    onTertiary = Grape20,
    tertiaryContainer = Grape30,
    onTertiaryContainer = Grape90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = Neutral8,
    onBackground = Neutral92,
    surface = Neutral8,
    onSurface = Neutral92,
    surfaceVariant = NeutralVariant30,
    onSurfaceVariant = NeutralVariant80,
    surfaceTint = Pomegranate80,
    inverseSurface = Neutral92,
    inverseOnSurface = Neutral20,
    outline = NeutralVariant60,
    outlineVariant = NeutralVariant30,
    scrim = Color.Black,
    surfaceBright = Neutral24,
    surfaceDim = Neutral8,
    surfaceContainerLowest = Neutral6,
    surfaceContainerLow = Neutral10,
    surfaceContainer = Neutral12,
    surfaceContainerHigh = Neutral17,
    surfaceContainerHighest = Neutral22,
)

/**
 * Georgian Alphabet theme.
 *
 * @param darkTheme Whether the theme should use a dark color scheme (follows system by default).
 * @param disableDynamicTheming If `true`, disables the use of dynamic theming, even when it is
 *        supported. Learning colors (mastery levels, correct and wrong answers) stay on the
 *        fixed palette either way.
 */
@Composable
fun GeAlphTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    disableDynamicTheming: Boolean = true,
    content: @Composable () -> Unit,
) {
    val useDynamicTheming = !disableDynamicTheming && supportsDynamicTheming()
    // Color scheme
    val colorScheme = when {
        useDynamicTheming -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> if (darkTheme) DarkDefaultColorScheme else LightDefaultColorScheme
    }
    // Learning colors
    val learningColors = if (darkTheme) DarkLearningColors else LightLearningColors
    // Background theme
    val backgroundTheme = BackgroundTheme(
        color = colorScheme.background,
        tonalElevation = if (useDynamicTheming) 2.dp else 0.dp,
    )
    val tintTheme = when {
        useDynamicTheming -> TintTheme(colorScheme.primary)
        else -> TintTheme()
    }
    // Composition locals
    CompositionLocalProvider(
        LocalLearningColors provides learningColors,
        LocalBackgroundTheme provides backgroundTheme,
        LocalTintTheme provides tintTheme,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = GeAlphTypography,
            shapes = GeAlphShapes,
            content = content,
        )
    }
}

/**
 * Shortcuts to the theme values that live outside [MaterialTheme], used as
 * `GeAlphTheme.learningColors.masteryMaster`.
 */
object GeAlphTheme {
    val learningColors: LearningColors
        @Composable
        @ReadOnlyComposable
        get() = LocalLearningColors.current

    val backgroundTheme: BackgroundTheme
        @Composable
        @ReadOnlyComposable
        get() = LocalBackgroundTheme.current

    val tintTheme: TintTheme
        @Composable
        @ReadOnlyComposable
        get() = LocalTintTheme.current
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
