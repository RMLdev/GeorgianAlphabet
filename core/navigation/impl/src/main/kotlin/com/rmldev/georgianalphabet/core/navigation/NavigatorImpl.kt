package com.rmldev.georgianalphabet.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.rmldev.georgianalphabet.core.navigation.api.NavigationCommand
import com.rmldev.georgianalphabet.core.navigation.api.NavigationInterceptor
import com.rmldev.georgianalphabet.core.navigation.api.NavigationKey
import com.rmldev.georgianalphabet.core.navigation.api.Navigator
import com.rmldev.georgianalphabet.core.navigation.api.ScreenOverlay
import com.rmldev.georgianalphabet.core.navigation.state.NavigationState

internal class NavigatorImpl(
    private val navigationState: NavigationState,
    private val interceptors: List<NavigationInterceptor>,
    private val onExit: () -> Unit
) : Navigator {

    override fun navigateTo(key: NavigationKey) = dispatch(NavigationCommand.Push(key))
    override fun back() = dispatch(NavigationCommand.Back)
    override fun popWhere(predicate: (NavigationKey) -> Boolean) =
        dispatch(NavigationCommand.PopWhere(predicate))

    override fun truncateEverywhere(predicate: (NavigationKey) -> Boolean) {
        dispatch(NavigationCommand.TruncateEverywhere(predicate))
    }

    private fun dispatch(command: NavigationCommand) {
        val context = navigationState.toNavigationContext()
        val final = interceptors.fold(command) { cmd, interceptor ->
            interceptor.intercept(cmd, context)
        }
        execute(final)
    }

    private fun execute(command: NavigationCommand): Unit = when (command) {
        is NavigationCommand.Push -> navigationState.pushKey(command.key)
        is NavigationCommand.Replace -> navigationState.replaceTop(command.key)
        NavigationCommand.Back -> executeBack()
        is NavigationCommand.PopWhere -> navigationState.popWhere(command.predicate)
        is NavigationCommand.TruncateEverywhere -> navigationState.truncateEverywhere(command.predicate)
        is NavigationCommand.Batch -> command.commands.forEach(::execute)   // without running interceptors again
        NavigationCommand.None -> Unit
    }

    private fun executeBack(): Unit = with(navigationState) {
        when {
            screenOverlay == ScreenOverlay.Modal && overlayBackStack.size == 1 -> navigationState.switchToMain()
            screenOverlay == ScreenOverlay.Blocking && overlayBackStack.size == 1 -> onExit()
            screenOverlay != ScreenOverlay.Hidden -> overlayBackStack.removeAt(overlayBackStack.lastIndex)
            currentTab != startTab && currentTabBackStack.size == 1 -> selectTab(startTab)
            currentTab == startTab && currentTabBackStack.size == 1 -> onExit()
            else -> currentTabBackStack.removeAt(currentTabBackStack.lastIndex)
        }
    }
}

@Composable
fun rememberNavigator(
    state: NavigationState,
    interceptors: List<NavigationInterceptor>,
    onExit: () -> Unit
): Navigator {
    return remember(state, interceptors) {
        NavigatorImpl(
            navigationState = state,
            interceptors = interceptors,
            onExit = onExit
        )
    }
}
