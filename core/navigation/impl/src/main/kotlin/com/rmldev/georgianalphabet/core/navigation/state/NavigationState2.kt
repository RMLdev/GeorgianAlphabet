package com.rmldev.georgianalphabet.core.navigation.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import com.rmldev.georgianalphabet.core.navigation.EmptyBackStackException
import com.rmldev.georgianalphabet.core.navigation.api.BackStackEntry
import com.rmldev.georgianalphabet.core.navigation.api.NavigationContext
import com.rmldev.georgianalphabet.core.navigation.api.NavigationKey
import com.rmldev.georgianalphabet.core.navigation.api.OverlayScreenKey
import com.rmldev.georgianalphabet.core.navigation.api.ScreenOverlay
import com.rmldev.georgianalphabet.core.navigation.api.TabKey
import com.rmldev.georgianalphabet.core.navigation.backstack.DjcBackStack
import com.rmldev.georgianalphabet.core.navigation.backstack.rememberDjcNavBackStack
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.SerializersModuleBuilder

@Stable
class NavigationState2(
    screenOverlayState: MutableState<ScreenOverlay>,
    currentTabState: MutableState<TabKey>,
    val startTab: TabKey,
    val overlayBackStack: DjcBackStack<NavigationKey>,
    val backStacksByTabs: Map<TabKey, DjcBackStack<NavigationKey>>,
) {
    var screenOverlay: ScreenOverlay by screenOverlayState
        private set

    var currentTab: TabKey by currentTabState
        private set

    val currentTabBackStack: DjcBackStack<NavigationKey>
        get() = backStacksByTabs.getValue(currentTab)

    val visibleBackStack: DjcBackStack<NavigationKey>
        get() = if (screenOverlay == ScreenOverlay.Hidden) currentTabBackStack else overlayBackStack

    val currentEntry: NavigationKey by derivedStateOf {
        visibleBackStack.lastOrNull()?.key ?: throw EmptyBackStackException()
    }

    fun selectTab(tab: TabKey) {
        require(tab in backStacksByTabs) { "Unknown tab: $tab" }
        currentTab = tab
    }

    fun pushKey(key: NavigationKey) {
        if (key.tag == visibleBackStack.lastOrNull()?.key?.tag) return

        when (key) {
            is TabKey -> {
                if (currentTab == key && screenOverlay == ScreenOverlay.Hidden) {
                    currentTabBackStack.resetToRoot()
                } else {
                    selectTab(key)
                    switchToMain()
                }
            }

            is OverlayScreenKey -> {
                switchToOverlay(key)
            }

            else -> {
                currentTabBackStack.add(BackStackEntry.new(key))
                switchToMain()
            }
        }
    }

    fun switchToOverlay(key: OverlayScreenKey) {
        check(key.overlayMode != ScreenOverlay.Hidden)
        overlayBackStack.add(BackStackEntry.new(key))
        if (screenOverlay == ScreenOverlay.Hidden) {
            screenOverlay = key.overlayMode
        }
    }

    fun switchToMain() {
        screenOverlay = ScreenOverlay.Hidden
        overlayBackStack.clear()
    }

    fun replaceTop(key: NavigationKey) {
        visibleBackStack.removeAt(overlayBackStack.lastIndex)
        visibleBackStack.add(BackStackEntry.new(key))
    }

    fun popWhere(predicate: (NavigationKey) -> Boolean) {
        val stack = currentTabBackStack
        var keep = stack.size
        while (keep > 1 && predicate(stack[keep - 1].key)) keep--
        if (keep < stack.size) stack.subList(keep, stack.size).clear()
    }

    fun truncateEverywhere(predicate: (NavigationKey) -> Boolean) {
        check(!predicate(startTab)) { "Start tab $startTab can't be removed" }

        backStacksByTabs.values.forEach { stack ->
            val index = stack.indexOfFirst { predicate(it.key) }
            when (index) {
                -1 -> Unit
                0 -> stack.resetToRoot()
                else -> stack.subList(index, stack.size).clear()
            }
        }
        if (predicate(currentTab)) selectTab(startTab)
    }

    fun toNavigationContext(): NavigationContext {
        return object : NavigationContext {
            override val currentTab: TabKey
                get() = this@NavigationState2.currentTab

            override val currentBackStack: List<NavigationKey>
                get() = this@NavigationState2.visibleBackStack.toList().map { it.key }

            override fun getStackByTab(tabKey: TabKey): List<NavigationKey>? {
                return backStacksByTabs[tabKey]?.toList()?.map { it.key }
            }
        }
    }
}

@Composable
fun rememberNavigationState(
    isFirstLaunch: Boolean,
    keySerializerRegistrars: Set<SerializersModuleBuilder.() -> Unit>,
    startTab: TabKey,
    tabKeys: Set<TabKey>,
    startKey: NavigationKey = startTab,
): NavigationState2 {
    val configuration = rememberNavSavedStateConfiguration(keySerializerRegistrars)
    val backStacksByTabs = tabKeys.associateWith { tab ->
        key(tab) { rememberDjcNavBackStack(configuration, tab) }
    }

    val overlayBackStack = rememberDjcNavBackStack(configuration, startKey)

    val screenOverlayState = rememberSerializable(
        configuration = configuration,
    ) {
        mutableStateOf(if (isFirstLaunch) ScreenOverlay.Blocking else ScreenOverlay.Hidden)
    }

    val currentTabState = rememberSerializable(
        serializer = MutableStateSerializer(PolymorphicSerializer(TabKey::class)),
        configuration = configuration,
    ) { mutableStateOf(startTab) }

    return remember(startTab, startKey, tabKeys) {
        NavigationState2(
            screenOverlayState = screenOverlayState,
            currentTabState = currentTabState,
            startTab = startTab,
            overlayBackStack = overlayBackStack,
            backStacksByTabs = backStacksByTabs,
        )
    }
}

@Composable
private fun rememberNavSavedStateConfiguration(
    registrars: Set<SerializersModuleBuilder.() -> Unit>,
): SavedStateConfiguration = remember(registrars) {
    SavedStateConfiguration {
        serializersModule = SerializersModule {
            registrars.forEach { it() }
        }
    }
}