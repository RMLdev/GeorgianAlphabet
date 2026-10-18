package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph

import com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui.StageCollapse
import org.junit.Test
import kotlin.test.assertEquals

class StageCollapseTest {

    private fun collapse(scrolled: Float) = StageCollapse(scrolled = { scrolled }, distance = 100f)

    @Test
    fun `at the top the glyph is whole and the title is hidden`() {
        val collapse = collapse(0f)
        assertEquals(0f, collapse.heroProgress, DELTA)
        assertEquals(0f, collapse.titleProgress, DELTA)
    }

    @Test
    fun `glyph fades out over the first 70 percent`() {
        assertEquals(0.5f, collapse(35f).heroProgress, DELTA)
        assertEquals(1f, collapse(70f).heroProgress, DELTA)
        assertEquals(1f, collapse(300f).heroProgress, DELTA)
    }

    @Test
    fun `title appears between 60 and 90 percent`() {
        assertEquals(0f, collapse(60f).titleProgress, DELTA)
        assertEquals(0.5f, collapse(75f).titleProgress, DELTA)
        assertEquals(1f, collapse(90f).titleProgress, DELTA)
    }

    @Test
    fun `stage not taller than the toolbar counts as collapsed`() {
        val collapse = StageCollapse(scrolled = { 0f }, distance = 0f)
        assertEquals(1f, collapse.heroProgress, DELTA)
        assertEquals(1f, collapse.titleProgress, DELTA)
    }

    private companion object {
        const val DELTA = 0.001f
    }
}
