package com.rmldev.georgianalphabet.core.audio

import com.rmldev.georgianalphabet.core.audio.api.AudioAssets
import com.rmldev.georgianalphabet.core.audio.api.SoundNotFoundException
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SoundPlayerImplTest {

    private val letter = AudioAssets.letter(1)
    private val word = AudioAssets.word("ana")

    private val factory = FakePlaybackFactory(available = setOf(letter, word))
    private val focus = FakeAudioFocus()

    private fun TestScope.player() = SoundPlayerImpl(
        playbackFactory = factory,
        audioFocus = focus,
        ioDispatcher = StandardTestDispatcher(testScheduler),
    )

    @Test
    fun `play starts sound and holds focus until it finishes`() = runTest {
        val player = player()

        assertTrue(player.play(letter).isSuccess)
        assertEquals(letter, player.playing.value)
        assertTrue(focus.held)

        factory.started.single().finish()

        assertNull(player.playing.value)
        assertFalse(focus.held)
        assertTrue(factory.started.single().released)
    }

    @Test
    fun `new sound interrupts previous one`() = runTest {
        val player = player()

        player.play(letter)
        player.play(word)

        val (first, second) = factory.started
        assertTrue(first.released)
        assertFalse(second.released)
        assertEquals(word, player.playing.value)
        assertTrue(focus.held)
    }

    @Test
    fun `late finish of interrupted sound does not stop the new one`() = runTest {
        val player = player()

        player.play(letter)
        player.play(word)
        factory.started.first().finish()

        assertEquals(word, player.playing.value)
        assertFalse(factory.started.last().released)
    }

    @Test
    fun `missing file fails silently and leaves player idle`() = runTest {
        val player = player()

        val result = player.play(AudioAssets.letter(99))

        val error = assertIs<SoundNotFoundException>(result.exceptionOrNull())
        assertEquals(AudioAssets.letter(99), error.assetPath)
        assertNull(player.playing.value)
        assertFalse(focus.held)
    }

    @Test
    fun `missing file stops the previous sound`() = runTest {
        val player = player()

        player.play(letter)
        player.play(AudioAssets.letter(99))

        assertTrue(factory.started.single().released)
        assertNull(player.playing.value)
        assertFalse(focus.held)
    }

    @Test
    fun `stop releases sound and focus`() = runTest {
        val player = player()

        player.play(letter)
        player.stop()

        assertNull(player.playing.value)
        assertFalse(focus.held)
        assertTrue(factory.started.single().released)
    }

    @Test
    fun `stop when idle does nothing`() = runTest {
        val player = player()

        player.stop()

        assertNull(player.playing.value)
        assertEquals(0, focus.abandonCount)
    }

    @Test
    fun `focus loss stops sound`() = runTest {
        val player = player()

        player.play(letter)
        focus.lose()

        assertNull(player.playing.value)
        assertTrue(factory.started.single().released)
    }

    @Test
    fun `denied focus fails without playing`() = runTest {
        val player = player()
        focus.grant = false

        val result = player.play(letter)

        assertTrue(result.isFailure)
        assertTrue(factory.started.isEmpty())
        assertNull(player.playing.value)
    }

    private class FakePlaybackFactory(private val available: Set<String>) : PlaybackFactory {

        val started = mutableListOf<FakePlayback>()

        override fun start(assetPath: String, onFinished: () -> Unit): Playback {
            if (assetPath !in available) throw SoundNotFoundException(assetPath)
            return FakePlayback(onFinished).also(started::add)
        }
    }

    private class FakePlayback(private val onFinished: () -> Unit) : Playback {

        var released = false
            private set

        fun finish() = onFinished()

        override fun release() {
            released = true
        }
    }

    private class FakeAudioFocus : AudioFocus {

        var grant = true
        var held = false
            private set
        var abandonCount = 0
            private set
        private var onLoss: (() -> Unit)? = null

        override fun request(onLoss: () -> Unit): Boolean {
            held = grant
            this.onLoss = onLoss.takeIf { grant }
            return grant
        }

        override fun abandon() {
            held = false
            abandonCount++
        }

        fun lose() {
            held = false
            onLoss?.invoke()
        }
    }
}
