package com.rmldev.georgianalphabet.core.designsystem.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

/**
 * Georgian Alphabet icons.
 *
 * Most are the stroke icons from the design mockups (24 dp, round caps and joins);
 * a few that the mockups don't draw yet stay Material Rounded.
 * The paths are drawn in black and recolored by the `tint` of `Icon`.
 */
object GeAlphIcons {
    // Navigation
    val Home: ImageVector by lazy {
        strokeIcon("Home", "M3 10.5 12 3l9 7.5V20a1 1 0 0 1-1 1h-5v-6H9v6H4a1 1 0 0 1-1-1z", width = 1.8f)
    }

    /** The letter ani (ა) from Noto Serif Georgian SemiBold, for the "Алфавит" tab. */
    val Alphabet: ImageVector by lazy {
        fillIcon(
            "Alphabet",
            "M11.95 21Q10.1 21 8.72 20.56Q7.33 20.12 6.41 19.32Q5.49 18.52 5.04 17.48Q4.58 16.44 4.58 15.24" +
                "Q4.58 14.25 4.95 13.45Q5.32 12.64 5.98 12.15Q6.64 11.66 7.49 11.66Q7.94 11.66 8.44 11.8" +
                "Q8.93 11.95 9.33 12.21Q9 12.92 8.86 13.6Q8.73 14.28 8.73 15.19Q8.73 16.37 9.1 17.25" +
                "Q9.48 18.13 10.21 18.62Q10.94 19.1 11.95 19.1Q13.01 19.1 13.72 18.61Q14.43 18.11 14.79 17.18" +
                "Q15.15 16.24 15.15 14.94Q15.15 13.79 14.82 12.94Q14.5 12.08 13.97 11.41Q13.45 10.73 12.83 10.15" +
                "Q12.14 9.47 11.47 8.75Q10.8 8.03 10.37 7.18Q9.93 6.32 9.93 5.25Q9.93 4.21 10.53 3.6" +
                "Q11.13 3 12.13 3Q12.76 3 13.2 3.14Q13.64 3.28 13.93 3.5Q13.72 3.86 13.62 4.26" +
                "Q13.52 4.65 13.52 5.09Q13.52 6.19 14.19 6.98Q14.86 7.76 15.75 8.54Q16.6 9.25 17.44 10.08" +
                "Q18.28 10.92 18.85 12.06Q19.42 13.19 19.42 14.84Q19.42 16.79 18.44 18.18" +
                "Q17.47 19.56 15.79 20.28Q14.11 21 11.95 21Z",
        )
    }
    val Games: ImageVector by lazy {
        strokeIcon(
            "Games",
            "M7.5 7H16.5A5.5 5.5 0 0 1 22 12.5A5.5 5.5 0 0 1 16.5 18H7.5A5.5 5.5 0 0 1 2 12.5A5.5 5.5 0 0 1 7.5 7Z",
            "M7 10.5v4M5 12.5h4M15.5 11.5h.01M18 14h.01",
            width = 1.8f,
        )
    }
    val Profile: ImageVector by lazy {
        strokeIcon("Profile", "M8 8A4 4 0 1 0 16 8A4 4 0 1 0 8 8Z", "M4 21a8 8 0 0 1 16 0", width = 1.8f)
    }

    // Top bars and pager
    val ArrowBack: ImageVector by lazy { strokeIcon("ArrowBack", "M19 12H5M12 19l-7-7 7-7", autoMirror = true) }
    val ChevronLeft: ImageVector by lazy { strokeIcon("ChevronLeft", "M15 18l-6-6 6-6", autoMirror = true) }
    val ChevronRight: ImageVector by lazy { strokeIcon("ChevronRight", "M9 18l6-6-6-6", autoMirror = true) }
    val AllFonts: ImageVector by lazy {
        strokeIcon(
            "AllFonts",
            "M5.5 3.5H8.5A2 2 0 0 1 10.5 5.5V8.5A2 2 0 0 1 8.5 10.5H5.5A2 2 0 0 1 3.5 8.5V5.5A2 2 0 0 1 5.5 3.5Z",
            "M15.5 3.5H18.5A2 2 0 0 1 20.5 5.5V8.5A2 2 0 0 1 18.5 10.5H15.5A2 2 0 0 1 13.5 8.5V5.5A2 2 0 0 1 15.5 3.5Z",
            "M5.5 13.5H8.5A2 2 0 0 1 10.5 15.5V18.5A2 2 0 0 1 8.5 20.5H5.5A2 2 0 0 1 3.5 18.5V15.5A2 2 0 0 1 5.5 13.5Z",
            "M15.5 13.5H18.5A2 2 0 0 1 20.5 15.5V18.5A2 2 0 0 1 18.5 20.5H15.5A2 2 0 0 1 13.5 18.5V15.5A2 2 0 0 1 15.5 13.5Z",
            width = 1.8f,
        )
    }
    val Shuffle: ImageVector by lazy { strokeIcon("Shuffle", "M16 3h5v5M4 20 21 3M21 16v5h-5M15 15l6 6M4 4l5 5") }

    // Sound
    val SoundLoud: ImageVector by lazy {
        strokeIcon("SoundLoud", "M11 5 6 9H3v6h3l5 4z", "M15.5 8.5a5 5 0 0 1 0 7M18.5 5.5a9 9 0 0 1 0 13")
    }
    val Sound: ImageVector by lazy { strokeIcon("Sound", "M11 5 6 9H3v6h3l5 4z", "M15.5 8.5a5 5 0 0 1 0 7") }

    // States and marks
    val Check: ImageVector by lazy { strokeIcon("Check", "M5 12l5 5L20 7", width = 2.2f) }
    val Lock: ImageVector by lazy {
        strokeIcon(
            "Lock",
            "M7.5 11H16.5A2.5 2.5 0 0 1 19 13.5V18.5A2.5 2.5 0 0 1 16.5 21H7.5A2.5 2.5 0 0 1 5 18.5V13.5A2.5 2.5 0 0 1 7.5 11Z",
            "M8 11V7.5a4 4 0 0 1 8 0V11",
            width = 1.8f,
        )
    }
    val Star: ImageVector by lazy {
        fillIcon("Star", "M12 2.5l2.9 6 6.6.8-4.9 4.6 1.3 6.6L12 17.3l-5.9 3.2 1.3-6.6-4.9-4.6 6.6-.8z")
    }

    // Onboarding and letter card
    val Travel: ImageVector by lazy {
        strokeIcon(
            "Travel",
            "M5.5 7H18.5A2.5 2.5 0 0 1 21 9.5V17.5A2.5 2.5 0 0 1 18.5 20H5.5A2.5 2.5 0 0 1 3 17.5V9.5A2.5 2.5 0 0 1 5.5 7Z",
            "M9 7V5.5A1.5 1.5 0 0 1 10.5 4h3A1.5 1.5 0 0 1 15 5.5V7M3 12h18",
            width = 1.8f,
        )
    }
    val Language: ImageVector by lazy {
        strokeIcon("Language", "M4 19.5v-15A2.5 2.5 0 0 1 6.5 2H20v20H6.5a2.5 2.5 0 0 1 0-5H20", width = 1.8f)
    }
    val Schedule: ImageVector by lazy {
        strokeIcon("Schedule", "M3 12A9 9 0 1 0 21 12A9 9 0 1 0 3 12Z", "M12 7v5l3 2", width = 1.8f)
    }
    val Notifications: ImageVector by lazy {
        strokeIcon(
            "Notifications",
            "M6 8a6 6 0 0 1 12 0c0 7 3 9 3 9H3s3-2 3-9",
            "M10.3 21a1.94 1.94 0 0 0 3.4 0",
            width = 1.8f,
        )
    }
    val Articulation: ImageVector by lazy {
        strokeIcon(
            "Articulation",
            "M8 4c-3 1.5-4 4.5-4 8 0 4 2 7 5 8M12 20c4 0 8-2.5 8-8 0-4-2.5-8-7-8-2.5 0-4 1.5-4 3.5S10.5 11 12 11",
            width = 1.8f,
        )
    }

    // Not in the mockups yet
    val Add = Icons.Rounded.Add
    val Close = Icons.Rounded.Close
    val MoreVert = Icons.Default.MoreVert
    val Search = Icons.Rounded.Search
    val Settings = Icons.Rounded.Settings
}

private fun strokeIcon(
    name: String,
    vararg paths: String,
    width: Float = 2f,
    autoMirror: Boolean = false,
): ImageVector = ImageVector.Builder(
    name = name,
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f,
    autoMirror = autoMirror,
).apply {
    paths.forEach { path ->
        addPath(
            pathData = addPathNodes(path),
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = width,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        )
    }
}.build()

private fun fillIcon(name: String, path: String): ImageVector = ImageVector.Builder(
    name = name,
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f,
).apply {
    addPath(pathData = addPathNodes(path), fill = SolidColor(Color.Black))
}.build()
