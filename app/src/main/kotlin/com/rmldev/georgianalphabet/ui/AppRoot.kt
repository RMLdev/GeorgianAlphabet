package com.rmldev.georgianalphabet.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.result.rememberResultEventBus
import androidx.navigation3.ui.NavDisplay
import com.rmldev.georgianalphabet.core.navigation.api.BackStackEntry
import com.rmldev.georgianalphabet.core.navigation.api.NavigationKey
import com.rmldev.georgianalphabet.core.navigation.api.Navigator
import com.rmldev.georgianalphabet.core.navigation.api.ScreenOverlay
import com.rmldev.georgianalphabet.core.navigation.state.NavigationState
import com.rmldev.georgianalphabet.core.navigation.state.toEntries
import com.rmldev.georgianalphabet.navigation.TOP_LEVEL_ITEMS

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AppRoot(
    navigationState: NavigationState,
    navigator: Navigator,
    entryProvider: (NavigationKey) -> NavEntry<NavigationKey>,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfoV2(),
) {
    val layoutType = NavigationSuiteScaffoldDefaults
        .navigationSuiteType(windowAdaptiveInfo)

    val navigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState()

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            TOP_LEVEL_ITEMS.forEach { (navKey, navItem) ->
                val selected = navigationState.currentTab == navKey

                item(
                    selected = selected,
                    onClick = { navigator.navigateTo(navKey) },
                    icon = {
                        if (selected) {
                            Icon(
                                imageVector = navItem.selectedIcon,
                                contentDescription = null,
                            )
                        } else {
                            Icon(
                                imageVector = navItem.unselectedIcon,
                                contentDescription = null,
                            )
                        }
                    },
                    label = { Text(stringResource(navItem.iconTextId)) },
                )
            }
        },
        layoutType = layoutType,
        containerColor = MaterialTheme.colorScheme.background,
        state = navigationSuiteScaffoldState
    ) {
        val listDetailStrategy = rememberListDetailSceneStrategy<BackStackEntry<NavigationKey>>()

        val resultEventBus = rememberResultEventBus()

        LaunchedEffect(navigationState.screenOverlay) {
            if (navigationState.screenOverlay != ScreenOverlay.Hidden) {
                navigationSuiteScaffoldState.hide()
            } else {
                navigationSuiteScaffoldState.show()
            }
        }

        BackHandler(enabled = navigationState.visibleBackStack.size == 1) { navigator.back() }

        NavDisplay(
            entries = navigationState.toEntries(entryProvider, resultEventBus),
            sceneStrategies = listOf(listDetailStrategy),
            onBack = { navigator.back() },
            predictivePopTransitionSpec = { swipeEdge ->
                ContentTransform(
                    fadeIn(animationSpec = tween(DEFAULT_TRANSITION_DURATION_MILLISECOND)),
                    fadeOut(animationSpec = tween(DEFAULT_TRANSITION_DURATION_MILLISECOND)),
                )
            }
        )
    }
}

const val DEFAULT_TRANSITION_DURATION_MILLISECOND = 700