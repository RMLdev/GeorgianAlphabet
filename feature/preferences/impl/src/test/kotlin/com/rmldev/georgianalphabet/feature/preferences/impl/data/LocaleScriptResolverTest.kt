package com.rmldev.georgianalphabet.feature.preferences.impl.data

import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript
import org.junit.Test
import java.util.Locale
import kotlin.test.assertEquals

class LocaleScriptResolverTest {

    private fun scriptFor(tag: String) = LocaleScriptResolver { Locale.forLanguageTag(tag) }.defaultScript()

    @Test
    fun `cyrillic languages map to cyrillic`() {
        listOf("ru-RU", "uk-UA", "be-BY", "kk-KZ", "bg", "sr-Cyrl-RS").forEach {
            assertEquals(KnownScript.CYRILLIC, scriptFor(it), it)
        }
    }

    @Test
    fun `other languages map to latin`() {
        listOf("en-US", "de-DE", "ka-GE", "tr", "sr-Latn-RS", "uz-Latn").forEach {
            assertEquals(KnownScript.LATIN, scriptFor(it), it)
        }
    }
}
