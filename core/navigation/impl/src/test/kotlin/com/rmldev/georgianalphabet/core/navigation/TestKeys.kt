package com.rmldev.georgianalphabet.core.navigation

import com.rmldev.georgianalphabet.core.navigation.api.NavigationKey
import com.rmldev.georgianalphabet.core.navigation.api.OverlayScreenKey
import com.rmldev.georgianalphabet.core.navigation.api.ScreenOverlay
import com.rmldev.georgianalphabet.core.navigation.api.TabKey
import com.rmldev.georgianalphabet.core.navigation.backstack.DjcBackStack
import com.rmldev.georgianalphabet.core.navigation.state.NavigationState2
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("HomeTab")
internal data object HomeTab : TabKey {
    override val tag: String get() = "HomeTab"
}

@Serializable
@SerialName("ProfileTab")
internal data object ProfileTab : TabKey {
    override val tag: String get() = "ProfileTab"
}

@Serializable
@SerialName("Details")
internal data class Details(val id: Int) : NavigationKey {
    override val tag: String get() = "Details"
}

@Serializable
@SerialName("Settings")
internal data object Settings : NavigationKey {
    override val tag: String get() = "Settings"
}

@Serializable
@SerialName("ModalScreen")
internal data class ModalScreen(val id: Int) : OverlayScreenKey {
    override val tag: String get() = "ModalScreen"
    override val overlayMode: ScreenOverlay get() = ScreenOverlay.Modal
}

@Serializable
@SerialName("BlockingScreen")
internal data object BlockingScreen : OverlayScreenKey {
    override val tag: String get() = "BlockingScreen"
    override val overlayMode: ScreenOverlay get() = ScreenOverlay.Blocking
}

/** State built the same way as in rememberNavigationState. */
internal fun navigationState(
    startTab: TabKey = HomeTab,
    tabs: List<TabKey> = listOf(HomeTab, ProfileTab),
    firstLaunchKey: OverlayScreenKey? = null,
): NavigationState2 = NavigationState2(
    screenOverlayState = mutableStateOf(if (firstLaunchKey != null) ScreenOverlay.Blocking else ScreenOverlay.Hidden),
    currentTabState = mutableStateOf(startTab),
    startTab = startTab,
    overlayBackStack = if (firstLaunchKey != null) DjcBackStack(firstLaunchKey) else DjcBackStack(),
    backStacksByTabs = tabs.associateWith { DjcBackStack(it) },
)

internal val NavigationState2.currentKeys: List<NavigationKey>
    get() = visibleBackStack.map { it.key }

internal fun NavigationState2.keysOf(tab: TabKey): List<NavigationKey> =
    backStacksByTabs.getValue(tab).map { it.key }
