package com.rmldev.georgianalphabet.core.navigation.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.result.ResultEventBus
import androidx.navigation3.runtime.result.rememberResultEventBusNavEntryDecorator
import com.rmldev.georgianalphabet.core.navigation.api.ScreenOverlay
import com.rmldev.georgianalphabet.core.navigation.api.BackStackEntry
import com.rmldev.georgianalphabet.core.navigation.api.NavigationKey
import com.rmldev.georgianalphabet.core.navigation.backstack.GeAlphBackStack

@Composable
fun NavigationState.toEntries(
    entryProvider: (NavigationKey) -> NavEntry<NavigationKey>,
    resultEventBus: ResultEventBus,
): List<NavEntry<BackStackEntry<NavigationKey>>> {
    val tabEntries = backStacksByTabs.mapValues { (tab, stack) ->
        key(tab) { rememberEntries(stack, entryProvider, resultEventBus) }
    }
    val overlayEntries = rememberEntries(overlayBackStack, entryProvider, resultEventBus)

    return when (screenOverlay) {
        ScreenOverlay.Hidden -> tabEntries.getValue(currentTab)
        ScreenOverlay.Modal -> tabEntries.getValue(currentTab) + overlayEntries
        ScreenOverlay.Blocking -> overlayEntries
    }
}

@Composable
private fun rememberEntries(
    stack: GeAlphBackStack<NavigationKey>,
    entryProvider: (NavigationKey) -> NavEntry<NavigationKey>,
    resultEventBus: ResultEventBus,
): List<NavEntry<BackStackEntry<NavigationKey>>> {
    val stackProvider: (BackStackEntry<NavigationKey>) -> NavEntry<BackStackEntry<NavigationKey>> =
        remember(entryProvider) {
            { stackEntry ->
                val inner = entryProvider(stackEntry.key)
                NavEntry(
                    key = stackEntry,
                    contentKey = stackEntry.id,
                    metadata = inner.metadata,
                ) { inner.Content() }
            }
        }

    val decorators = listOf(
        rememberSaveableStateHolderNavEntryDecorator<BackStackEntry<NavigationKey>>(),
        rememberViewModelStoreNavEntryDecorator<BackStackEntry<NavigationKey>>(),
        rememberResultEventBusNavEntryDecorator(resultEventBus),
    )
    return rememberDecoratedNavEntries(
        backStack = stack,
        entryDecorators = decorators,
        entryProvider = stackProvider,
    )
}