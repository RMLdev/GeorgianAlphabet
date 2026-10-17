package com.rmldev.georgianalphabet.core.navigation

import com.rmldev.georgianalphabet.core.navigation.api.NavigationCommand
import com.rmldev.georgianalphabet.core.navigation.api.NavigationContext
import com.rmldev.georgianalphabet.core.navigation.api.NavigationInterceptor
import com.rmldev.georgianalphabet.core.navigation.api.ScreenOverlay
import com.rmldev.georgianalphabet.core.navigation.state.NavigationState2
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NavigationStateBugsTest {

    private var exitCount = 0

    private fun navigator(state: NavigationState2, vararg interceptors: NavigationInterceptor) =
        NavigatorImpl(state, interceptors.toList(), onExit = { exitCount++ })

    private val replaceInterceptor = object : NavigationInterceptor {
        override fun intercept(command: NavigationCommand, context: NavigationContext) =
            if (command is NavigationCommand.Push && command.key is Settings) {
                NavigationCommand.Replace(command.key)
            } else {
                command
            }
    }

    @Test
    fun `replace swaps top of tab stack when overlay is hidden`() {
        val state = navigationState()
        val navigator = navigator(state, replaceInterceptor)

        navigator.navigateTo(Details(1))
        navigator.navigateTo(Details(2))
        navigator.navigateTo(Settings)

        assertEquals(listOf(HomeTab, Details(1), Settings), state.currentKeys)
    }

    @Test
    fun `replace swaps top of overlay stack when overlay is shown`() {
        val state = navigationState()
        val navigator = navigator(state, replaceInterceptor)

        navigator.navigateTo(ModalScreen(1))
        navigator.navigateTo(ModalScreen(2))
        navigator.navigateTo(Settings)

        assertEquals(listOf(ModalScreen(1), Settings), state.currentKeys)
        assertEquals(listOf(HomeTab), state.keysOf(HomeTab))
    }

    @Test
    fun `same key type with different arguments is pushed`() {
        val state = navigationState()
        val navigator = navigator(state)

        navigator.navigateTo(Details(1))
        navigator.navigateTo(Details(2))

        assertEquals(listOf(HomeTab, Details(1), Details(2)), state.currentKeys)
    }

    @Test
    fun `equal key on top is not pushed twice`() {
        val state = navigationState()
        val navigator = navigator(state)

        navigator.navigateTo(Details(1))
        navigator.navigateTo(Details(1))

        assertEquals(listOf(HomeTab, Details(1)), state.currentKeys)
    }

    @Test
    fun `overlay stack is empty while overlay is hidden`() {
        val state = navigationState()

        assertTrue(state.overlayBackStack.isEmpty())
    }

    @Test
    fun `back from single modal returns to tabs`() {
        val state = navigationState()
        val navigator = navigator(state)

        navigator.navigateTo(Details(1))
        navigator.navigateTo(ModalScreen(1))
        navigator.back()

        assertEquals(ScreenOverlay.Hidden, state.screenOverlay)
        assertEquals(listOf(HomeTab, Details(1)), state.currentKeys)
        assertTrue(state.overlayBackStack.isEmpty())
    }

    @Test
    fun `back inside modal stack pops one screen`() {
        val state = navigationState()
        val navigator = navigator(state)

        navigator.navigateTo(ModalScreen(1))
        navigator.navigateTo(ModalScreen(2))
        navigator.back()

        assertEquals(ScreenOverlay.Modal, state.screenOverlay)
        assertEquals(listOf(ModalScreen(1)), state.currentKeys)
    }

    @Test
    fun `first launch starts with blocking overlay and back exits`() {
        val state = navigationState(firstLaunchKey = BlockingScreen)

        assertEquals(ScreenOverlay.Blocking, state.screenOverlay)
        assertEquals(BlockingScreen, state.currentEntry)

        navigator(state).back()

        assertEquals(1, exitCount)
    }

    @Test
    fun `leaving first launch overlay to tab shows tab`() {
        val state = navigationState(firstLaunchKey = BlockingScreen)

        navigator(state).navigateTo(HomeTab)

        assertEquals(ScreenOverlay.Hidden, state.screenOverlay)
        assertEquals(listOf(HomeTab), state.currentKeys)
        assertTrue(state.overlayBackStack.isEmpty())
    }
}
