package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state

import androidx.lifecycle.SavedStateHandle

// Только выбор пользователя: букву, детали и прогресс Actor загружает заново
private const val KEY_FONT_ID = "glyph_details_font_id"

/** Шрифт, сохранённый до смерти процесса, иначе [navFontId] из NavKey. */
internal fun SavedStateHandle.restoreFontId(navFontId: String?): String? = get<String>(KEY_FONT_ID) ?: navFontId

internal fun SavedStateHandle.saveFontId(fontId: String?) {
    set(KEY_FONT_ID, fontId)
}
