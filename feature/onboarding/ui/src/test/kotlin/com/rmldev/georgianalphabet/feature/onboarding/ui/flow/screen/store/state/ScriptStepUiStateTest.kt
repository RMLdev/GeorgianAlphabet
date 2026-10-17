package com.rmldev.georgianalphabet.feature.onboarding.ui.flow.screen.store.state

import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript
import org.junit.Test
import kotlin.test.assertEquals

class ScriptStepUiStateTest {

    @Test
    fun `options follow known scripts and mark the selected one`() {
        val options = OnboardingState(knownScript = KnownScript.LATIN).toScriptStepUi().options

        assertEquals(KnownScript.entries, options.map { it.script })
        assertEquals(listOf(false, true), options.map { it.selected })
        assertEquals(listOf("Gg", "Qq", "Ww"), options.last().glyphs)
        assertEquals(listOf("ани", "ani"), options.map { it.example })
    }

    @Test
    fun `nothing is selected until the locale default is read`() {
        val options = OnboardingState(knownScript = null).toScriptStepUi().options

        assertEquals(listOf(false, false), options.map { it.selected })
    }
}
