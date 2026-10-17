package com.rmldev.georgianalphabet.core.designsystem.font

import org.junit.Test
import java.io.File
import java.nio.ByteBuffer
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FontCatalogTest {

    // Unit-тесты запускаются из папки модуля
    private val fontDir = File("src/main/res/font")
    private val licenseDir = File("src/main/assets/licenses/fonts")

    private val mkhedruli = (0x10D0..0x10F0).toSet()

    private companion object {
        // Карта уроков: 6 групп букв (concept.md 4)
        const val LESSON_COUNT = 6
        const val MKHEDRULI_COUNT = 33
    }

    private fun fontFile(font: GeorgianFont): File {
        val name = if (font.id ==
            FontCatalog.DEFAULT_FONT_ID
        ) {
            "noto_serif_georgian_regular"
        } else {
            "core_designsystem_${font.id}"
        }
        return fontDir.listFiles().orEmpty().single { it.nameWithoutExtension == name }
    }

    @Test
    fun `catalog has 15 fonts with unique ids and 5 starters`() {
        assertEquals(15, FontCatalog.all.size)
        assertEquals(15, FontCatalog.all.map { it.id }.toSet().size)
        assertEquals(5, FontCatalog.unlocked().size)
        assertTrue(FontCatalog.unlocked().all { it.difficulty == 1 })
        assertTrue(FontCatalog.all.all { it.difficulty in 1..3 })
    }

    @Test
    fun `every font has all 33 letters`() {
        FontCatalog.all.forEach { font ->
            val missing = mkhedruli - readCmap(fontFile(font))
            assertTrue(missing.isEmpty(), "${font.id} misses ${missing.joinToString("") { Character.toString(it) }}")
        }
    }

    @Test
    fun `every font has a license file and no extra fonts are bundled`() {
        FontCatalog.all.forEach { font ->
            assertTrue(File(licenseDir, "${font.id}.txt").isFile, "no license for ${font.id}")
        }
        assertEquals(FontCatalog.all.size, fontDir.listFiles().orEmpty().size)
    }

    @Test
    fun `unlocked adds rewarded fonts and ignores unknown ids`() {
        val unlocked = FontCatalog.unlocked(setOf("aznauri", "unknown"))

        assertEquals(6, unlocked.size)
        assertTrue(FontCatalog.byId("aznauri") in unlocked)
    }

    @Test
    fun `locked fonts have an unlock condition and starters do not`() {
        FontCatalog.all.forEach { font ->
            assertEquals(font.unlockedByDefault, font.unlockCondition == null, font.id)
        }
        assertEquals(10, FontCatalog.all.count { it.unlockCondition != null })
    }

    @Test
    fun `unlock conditions are valid and distinct`() {
        val conditions = FontCatalog.all.mapNotNull { it.unlockCondition }

        assertEquals(conditions.size, conditions.toSet().size)
        conditions.forEach { condition ->
            when (condition) {
                // Последний урок — это AllLessons
                is UnlockCondition.Lesson -> assertTrue(condition.number in 1 until LESSON_COUNT, "$condition")

                is UnlockCondition.Streak -> assertTrue(condition.days > 0, "$condition")

                is UnlockCondition.LettersMastered -> assertTrue(condition.count in 1..MKHEDRULI_COUNT, "$condition")

                UnlockCondition.AllLessons -> Unit
            }
        }
    }

    @Test
    fun `harder fonts never unlock by an earlier lesson than easier ones`() {
        fun lessons(difficulty: Int) = FontCatalog.all
            .filter { it.difficulty == difficulty }
            .mapNotNull { (it.unlockCondition as? UnlockCondition.Lesson)?.number }

        assertTrue(lessons(2).max() < (lessons(3).minOrNull() ?: Int.MAX_VALUE))
    }

    /** Кодовые точки из таблицы cmap (форматы 4 и 12) TrueType/OpenType-файла. */
    private fun readCmap(file: File): Set<Int> {
        val buf = ByteBuffer.wrap(file.readBytes())
        val numTables = buf.getShort(4).toInt() and 0xFFFF
        val cmapOffset = (0 until numTables).map { 12 + it * 16 }
            .first { String(ByteArray(4) { i -> buf.get(it + i) }) == "cmap" }
            .let { buf.getInt(it + 8) }
        val result = mutableSetOf<Int>()
        val subtables = buf.getShort(cmapOffset + 2).toInt() and 0xFFFF
        repeat(subtables) { index ->
            val sub = cmapOffset + buf.getInt(cmapOffset + 4 + index * 8 + 4)
            when (buf.getShort(sub).toInt() and 0xFFFF) {
                4 -> {
                    val segX2 = buf.getShort(sub + 6).toInt() and 0xFFFF
                    val ends = sub + 14
                    val starts = ends + segX2 + 2
                    for (s in 0 until segX2 / 2) {
                        val end = buf.getShort(ends + s * 2).toInt() and 0xFFFF
                        val start = buf.getShort(starts + s * 2).toInt() and 0xFFFF
                        if (start != 0xFFFF) result += start..end
                    }
                }

                12 -> {
                    val groups = buf.getInt(sub + 12)
                    for (g in 0 until groups) {
                        val base = sub + 16 + g * 12
                        result += buf.getInt(base)..buf.getInt(base + 4)
                    }
                }
            }
        }
        return result
    }
}
