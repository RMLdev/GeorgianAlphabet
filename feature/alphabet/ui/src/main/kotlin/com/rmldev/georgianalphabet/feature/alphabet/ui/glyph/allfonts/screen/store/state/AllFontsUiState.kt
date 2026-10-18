package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts.screen.store.state

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.rmldev.georgianalphabet.core.designsystem.font.FontCatalog
import com.rmldev.georgianalphabet.core.designsystem.font.GeorgianFont
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
internal data class AllFontsUiState(
    val letterId: Int = 0,
    /** Буква мхедрули; пусто, пока буква грузится или её нет. */
    val symbol: String = "",
    /** Название буквы в знакомом алфавите: «ани» или «ani». */
    val letterName: String = "",
    val openCount: Int = 0,
    val totalCount: Int = FontCatalog.all.size,
    /** Группы по сложности шрифта, от простых к декоративным; пустые группы не попадают. */
    val groups: ImmutableList<FontGroupUi> = persistentListOf(),
    val isNotFound: Boolean = false,
)

/** Шрифты одной сложности: 1 — простые, 2 — необычные, 3 — декоративные. */
@Immutable
internal data class FontGroupUi(val difficulty: Int, val openCount: Int, val fonts: ImmutableList<FontTileUi>)

/**
 * Плитка шрифта. Закрытый шрифт показывает условие открытия `font.unlockCondition`.
 *
 * @param isRecognized букву узнают в этом шрифте; только для открытого шрифта.
 */
@Immutable
internal data class FontTileUi(val font: GeorgianFont, val isOpen: Boolean, val isRecognized: Boolean = false)
