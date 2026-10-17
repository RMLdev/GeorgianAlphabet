package com.rmldev.georgianalphabet.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Georgian Alphabet shapes.
 *
 * Buttons, the sound button at rest, segmented buttons and progress bars are pills:
 * use CircleShape for them.
 */
internal val GeAlphShapes = Shapes(
    // Info pills (рус. / лат. / МФА), mastery segments
    extraSmall = RoundedCornerShape(8.dp),
    // Filter chips, font cells on the letter card
    small = RoundedCornerShape(10.dp),
    // List items, similar-letter chips, hint boxes
    medium = RoundedCornerShape(16.dp),
    // Letter tiles in the alphabet grid, info boxes, notification preview
    large = RoundedCornerShape(20.dp),
    // Cards: mastery card, onboarding options, font cards
    extraLarge = RoundedCornerShape(28.dp),
)

/**
 * Extra shapes outside the Material 3 scale.
 */
object GeAlphShapeDefaults {
    /** Bottom of the glyph stage on the letter card: the pager area that bleeds to the top edge. */
    val GlyphStage = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)

    /** Sound button and selected daily-goal card while active: the Expressive morph target. */
    val Morphed = RoundedCornerShape(36.dp)
}
