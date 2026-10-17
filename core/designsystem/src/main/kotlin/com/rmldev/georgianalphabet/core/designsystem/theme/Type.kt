package com.rmldev.georgianalphabet.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.LineHeightStyle.Alignment
import androidx.compose.ui.text.style.LineHeightStyle.Trim
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.rmldev.georgianalphabet.core.designsystem.R

private val GoogleFontsProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs,
)

private fun googleFontFamily(name: String): FontFamily {
    val font = GoogleFont(name)
    return FontFamily(
        Font(googleFont = font, fontProvider = GoogleFontsProvider, weight = FontWeight.Normal),
        Font(googleFont = font, fontProvider = GoogleFontsProvider, weight = FontWeight.Medium),
        Font(googleFont = font, fontProvider = GoogleFontsProvider, weight = FontWeight.SemiBold),
        Font(googleFont = font, fontProvider = GoogleFontsProvider, weight = FontWeight.Bold),
    )
}

/**
 * Rubik, the interface font, downloaded through Google Play services.
 * Until it is downloaded (or on devices without Play services) the system font is used.
 */
internal val Rubik = googleFontFamily("Rubik")

/**
 * Noto Sans Georgian for Georgian text in the interface: letter names (ანი),
 * example words, the notification glyph. Not for learning glyphs: those use the
 * app's font collection and [GlyphSize].
 */
val NotoSansGeorgian = googleFontFamily("Noto Sans Georgian")

/**
 * Sizes of learning glyphs, far larger than any style of the type scale.
 * A glyph is always centered on its optical center inside a square box, because the
 * collection fonts differ a lot in metrics.
 */
object GlyphSize {
    /** Font cells on the letter card. */
    val Mini: TextUnit = 20.sp

    /** Alphabet grid tiles, similar-letter chips. */
    val Tile: TextUnit = 36.sp

    /** "All fonts" cards, answer options with glyphs. */
    val Card: TextUnit = 60.sp

    /** Question glyph in a task. */
    val Task: TextUnit = 160.sp

    /** Letter card pager. */
    val Hero: TextUnit = 240.sp
}

/**
 * Georgian Alphabet typography: the Material 3 scale in Rubik.
 */
internal val GeAlphTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),
    // Daily goal numbers in onboarding ("10 мин")
    displaySmall = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),
    // Welcome title, large top bar title ("Алфавит"), letter name (ანი, with NotoSansGeorgian)
    headlineLarge = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.5).sp,
    ),
    // Onboarding step titles, "Все шрифты" title, big numbers ("5 из 15")
    headlineMedium = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    // Locked fonts page ("Ещё 8 шрифтов")
    headlineSmall = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = Alignment.Bottom,
            trim = Trim.None,
        ),
    ),
    // Empty and error states
    titleLarge = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = Alignment.Bottom,
            trim = Trim.LastLineBottom,
        ),
    ),
    // Section headers ("Примеры слов", "Архаичные буквы"), onboarding option titles
    titleMedium = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),
    // Group headers ("Урок 1", "Простые"), font name chip
    titleSmall = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp,
    ),
    // Default text style: subtitles, association phrase
    bodyLarge = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = Alignment.Center,
            trim = Trim.None,
        ),
    ),
    // Option hints, transliteration and translation of example words
    bodyMedium = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    // Letter name under a tile, legends, captions
    bodySmall = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    // Used for Button and chips
    labelLarge = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = Alignment.Center,
            trim = Trim.LastLineBottom,
        ),
    ),
    // Used for Navigation items and the "скоро" badge
    labelMedium = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = Alignment.Center,
            trim = Trim.LastLineBottom,
        ),
    ),
    // Unlock hints on locked font cards ("Урок 3")
    labelSmall = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = Alignment.Center,
            trim = Trim.LastLineBottom,
        ),
    ),
)
