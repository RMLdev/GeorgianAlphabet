package com.rmldev.georgianalphabet.core.designsystem.font

import androidx.annotation.FontRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.rmldev.georgianalphabet.core.designsystem.R
import com.rmldev.georgianalphabet.core.designsystem.theme.NotoSerifGeorgian

/**
 * Шрифт коллекции, в котором показываются буквы (concept.md 2.3).
 *
 * @param id стабильный id: хранится в прогрессе (`recognizedFontIds`), не менять.
 * @param difficulty 1 — привычное начертание, 2 — заметно стилизованное, 3 — декоративное.
 */
@Immutable
class GeorgianFont internal constructor(
    val id: String,
    val displayName: String,
    @param:FontRes val fontRes: Int,
    val difficulty: Int,
    val unlockedByDefault: Boolean,
) {
    val fontFamily: FontFamily = if (fontRes == R.font.noto_serif_georgian_regular) {
        NotoSerifGeorgian
    } else {
        FontFamily(Font(fontRes, FontWeight.Normal))
    }

    /** Файл лицензии в assets. */
    val licenseAssetPath: String get() = "licenses/fonts/$id.txt"

    override fun equals(other: Any?): Boolean = other is GeorgianFont && other.id == id

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "GeorgianFont($id)"
}

/**
 * Все шрифты приложения. Файлы в `res/font` обрезаны до грузинских букв (U+10D0–U+10FF и пробел),
 * кроме Aznauri и XI20: они под AGPL-3.0 и лежат без изменений, чтобы не распространять модифицированную версию;
 * в каждом есть все 33 буквы мхедрули (проверяет `FontCatalogTest`). Лицензии — в `assets/licenses/fonts`.
 * Royana, Cartoon 1471 и Garamond Typoart не включены, пока лицензия не согласована с авторами.
 */
object FontCatalog {

    const val DEFAULT_FONT_ID = "noto_serif_georgian"

    val all: List<GeorgianFont> = listOf(
        // Сложность 1: стартовые, открыты сразу
        GeorgianFont(DEFAULT_FONT_ID, "Noto Serif Georgian", R.font.noto_serif_georgian_regular, 1, true),
        GeorgianFont("dejavu_sans", "DejaVu Sans", R.font.core_designsystem_dejavu_sans, 1, true),
        GeorgianFont("k1fs", "K1FS", R.font.core_designsystem_k1fs, 1, true),
        GeorgianFont("rail_model", "Rail Model", R.font.core_designsystem_rail_model, 1, true),
        GeorgianFont("xi20", "XI20", R.font.core_designsystem_xi20, 1, true),
        // Сложность 2
        GeorgianFont("catrinity", "Catrinity", R.font.core_designsystem_catrinity, 2, false),
        GeorgianFont("quivira", "Quivira", R.font.core_designsystem_quivira, 2, false),
        GeorgianFont("truetypewriter", "Truetypewriter PolyglOTT", R.font.core_designsystem_truetypewriter, 2, false),
        GeorgianFont("galiver_sans", "Galiver Sans", R.font.core_designsystem_galiver_sans, 2, false),
        GeorgianFont("mkhedruli_grunge", "Mkhedruli Grunge", R.font.core_designsystem_mkhedruli_grunge, 2, false),
        // Сложность 3
        GeorgianFont("alk_life", "ALK Life", R.font.core_designsystem_alk_life, 3, false),
        GeorgianFont("aznauri", "Aznauri", R.font.core_designsystem_aznauri, 3, false),
        GeorgianFont("balhattan", "Balhattan", R.font.core_designsystem_balhattan, 3, false),
        GeorgianFont("balsobby_computer", "BalsObby Computer", R.font.core_designsystem_balsobby_computer, 3, false),
        GeorgianFont("vegesignes", "Vegesignes", R.font.core_designsystem_vegesignes, 3, false),
    )

    private val byId: Map<String, GeorgianFont> = all.associateBy { it.id }

    val default: GeorgianFont get() = byId.getValue(DEFAULT_FONT_ID)

    fun byId(id: String): GeorgianFont? = byId[id]

    /**
     * Открытые шрифты: стартовые плюс открытые наградой ([unlockedIds], хранятся у вызывающего).
     * Неизвестные id игнорируются.
     */
    fun unlocked(unlockedIds: Set<String> = emptySet()): List<GeorgianFont> =
        all.filter { it.unlockedByDefault || it.id in unlockedIds }
}
