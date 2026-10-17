package com.rmldev.georgianalphabet.feature.preferences.impl.data

import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript
import java.util.Locale
import javax.inject.Inject

/** Алфавит по умолчанию: кириллица для кириллических локалей, иначе латиница. */
internal class LocaleScriptResolver(private val locale: () -> Locale) {

    @Inject
    constructor() : this({ Locale.getDefault() })

    fun defaultScript(): KnownScript {
        val current = locale()
        val isCyrillic = current.script == CYRILLIC_SCRIPT ||
            (current.script.isEmpty() && current.language in CYRILLIC_LANGUAGES)
        return if (isCyrillic) KnownScript.CYRILLIC else KnownScript.LATIN
    }

    private companion object {
        const val CYRILLIC_SCRIPT = "Cyrl"

        // Языки, которые без явного скрипта пишутся кириллицей
        val CYRILLIC_LANGUAGES = setOf(
            "ru", "uk", "be", "bg", "mk", "sr", "kk", "ky", "tg", "mn", "tt", "ba", "cv", "ce", "os", "ab",
        )
    }
}
