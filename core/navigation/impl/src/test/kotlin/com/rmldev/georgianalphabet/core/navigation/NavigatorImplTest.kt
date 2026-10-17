package com.rmldev.georgianalphabet.core.navigation

import com.rmldev.georgianalphabet.core.navigation.api.NavigationCommand
import com.rmldev.georgianalphabet.core.navigation.api.NavigationContext
import com.rmldev.georgianalphabet.core.navigation.api.NavigationInterceptor
import com.rmldev.georgianalphabet.core.navigation.api.ScreenOverlay
import com.rmldev.georgianalphabet.core.navigation.state.NavigationState2
import org.junit.Test
import kotlin.test.assertEquals

class NavigatorImplTest {

    private var exitCount = 0

    private fun navigator(
        state: NavigationState2,
        interceptors: List<NavigationInterceptor> = emptyList(),
    ) = NavigatorImpl(state, interceptors, onExit = { exitCount++ })

    @Test
    fun `navigateTo pushes key on current tab`() {
        val state = navigationState()

        navigator(state).navigateTo(Details(1))

        assertEquals(listOf(HomeTab, Details(1)), state.currentKeys)
        assertEquals(Details(1), state.currentEntry)
    }

    @Test
    fun `navigateTo tab switches tab and keeps other tab stack`() {
        val state = navigationState()
        val navigator = navigator(state)

        navigator.navigateTo(Details(1))
        navigator.navigateTo(ProfileTab)

        assertEquals(ProfileTab, state.currentTab)
        assertEquals(listOf(ProfileTab), state.currentKeys)
        assertEquals(listOf(HomeTab, Details(1)), state.keysOf(HomeTab))
    }

    @Test
    fun `navigateTo current tab resets it to root`() {
        val state = navigationState()
        val navigator = navigator(state)

        navigator.navigateTo(Details(1))
        navigator.navigateTo(Settings)
        navigator.navigateTo(HomeTab)

        assertEquals(listOf(HomeTab), state.currentKeys)
    }

    @Test
    fun `back pops top of current tab`() {
        val state = navigationState()
        val navigator = navigator(state)

        navigator.navigateTo(Details(1))
        navigator.back()

        assertEquals(listOf(HomeTab), state.currentKeys)
        assertEquals(0, exitCount)
    }

    @Test
    fun `back on root of start tab exits`() {
        val state = navigationState()

        navigator(state).back()

        assertEquals(1, exitCount)
        assertEquals(listOf(HomeTab), state.currentKeys)
    }

    @Test
    fun `back on root of other tab returns to start tab`() {
        val state = navigationState()
        val navigator = navigator(state)

        navigator.navigateTo(ProfileTab)
        navigator.back()

        assertEquals(HomeTab, state.currentTab)
        assertEquals(0, exitCount)
    }

    @Test
    fun `navigateTo overlay key shows it above tabs`() {
        val state = navigationState()

        navigator(state).navigateTo(ModalScreen(1))

        assertEquals(ScreenOverlay.Modal, state.screenOverlay)
        assertEquals(ModalScreen(1), state.currentEntry)
    }

    @Test
    fun `navigateTo regular key from overlay closes overlay`() {
        val state = navigationState()
        val navigator = navigator(state)

        navigator.navigateTo(ModalScreen(1))
        navigator.navigateTo(Details(1))

        assertEquals(ScreenOverlay.Hidden, state.screenOverlay)
        assertEquals(listOf(HomeTab, Details(1)), state.currentKeys)
    }

    @Test
    fun `popWhere removes matching keys from the top but keeps root`() {
        val state = navigationState()
        val navigator = navigator(state)

        navigator.navigateTo(Settings)
        navigator.navigateTo(Details(1))
        navigator.navigateTo(Details(2))
        navigator.popWhere { it is Details }

        assertEquals(listOf(HomeTab, Settings), state.currentKeys)
    }

    @Test
    fun `popWhere never removes tab root`() {
        val state = navigationState()

        navigator(state).popWhere { true }

        assertEquals(listOf(HomeTab), state.currentKeys)
    }

    @Test
    fun `truncateEverywhere cuts every tab stack from first match`() {
        val state = navigationState()
        val navigator = navigator(state)

        navigator.navigateTo(Details(1))
        navigator.navigateTo(Settings)
        navigator.navigateTo(ProfileTab)
        navigator.navigateTo(Details(2))
        navigator.truncateEverywhere { it is Details }

        assertEquals(listOf(HomeTab), state.keysOf(HomeTab))
        assertEquals(listOf(ProfileTab), state.keysOf(ProfileTab))
    }

    @Test
    fun `truncateEverywhere on current tab root resets it and selects start tab`() {
        val state = navigationState()
        val navigator = navigator(state)

        navigator.navigateTo(ProfileTab)
        navigator.navigateTo(Settings)
        navigator.truncateEverywhere { it == ProfileTab }

        assertEquals(HomeTab, state.currentTab)
        assertEquals(listOf(ProfileTab), state.keysOf(ProfileTab))
    }

    @Test(expected = IllegalStateException::class)
    fun `truncateEverywhere can't remove start tab`() {
        navigator(navigationState()).truncateEverywhere { it == HomeTab }
    }

    @Test
    fun `interceptors are applied in order and receive navigation context`() {
        val state = navigationState()
        val seenStacks = mutableListOf<List<Any>>()
        val redirectToSettings = object : NavigationInterceptor {
            override fun intercept(command: NavigationCommand, context: NavigationContext): NavigationCommand {
                seenStacks += context.currentBackStack
                return if (command == NavigationCommand.Push(Details(1))) NavigationCommand.Push(Settings) else command
            }
        }
        val appendDetails = object : NavigationInterceptor {
            override fun intercept(command: NavigationCommand, context: NavigationContext): NavigationCommand =
                NavigationCommand.Batch(listOf(command, NavigationCommand.Push(Details(2))))
        }

        navigator(state, listOf(redirectToSettings, appendDetails)).navigateTo(Details(1))

        assertEquals(listOf(HomeTab, Settings, Details(2)), state.currentKeys)
        assertEquals(listOf(listOf<Any>(HomeTab)), seenStacks)
    }

    @Test
    fun `interceptor can cancel navigation`() {
        val state = navigationState()
        val cancelAll = object : NavigationInterceptor {
            override fun intercept(command: NavigationCommand, context: NavigationContext) = NavigationCommand.None
        }

        navigator(state, listOf(cancelAll)).navigateTo(Details(1))

        assertEquals(listOf(HomeTab), state.currentKeys)
    }

    @Test
    fun `navigation context exposes stacks by tab`() {
        val state = navigationState()
        navigator(state).navigateTo(Details(1))

        val context = state.toNavigationContext()

        assertEquals(HomeTab, context.currentTab)
        assertEquals(listOf(HomeTab, Details(1)), context.getStackByTab(HomeTab))
        assertEquals(listOf(ProfileTab), context.getStackByTab(ProfileTab))
    }
}
