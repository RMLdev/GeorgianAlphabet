package com.rmldev.georgianalphabet.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.entryProvider
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme
import com.rmldev.georgianalphabet.core.navigation.api.NavigationInterceptor
import com.rmldev.georgianalphabet.core.navigation.api.NavigationKey
import com.rmldev.georgianalphabet.core.navigation.api.Navigator
import com.rmldev.georgianalphabet.core.navigation.rememberNavigator
import com.rmldev.georgianalphabet.core.navigation.state.rememberNavigationState
import com.rmldev.georgianalphabet.feature.alphabet.api.AlphabetNavKey
import com.rmldev.georgianalphabet.navigation.TOP_LEVEL_ITEMS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.modules.SerializersModuleBuilder
import javax.inject.Inject

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {

    @Inject
    lateinit var entryBuilders: Set<@JvmSuppressWildcards EntryProviderScope<NavigationKey>.(Navigator) -> Unit>

    @Inject
    lateinit var keySerializerRegistrars: Set<@JvmSuppressWildcards SerializersModuleBuilder.() -> Unit>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navigationState = rememberNavigationState(
                isFirstLaunch = false,
                keySerializerRegistrars = keySerializerRegistrars,
                startTab = AlphabetNavKey,
                tabKeys = TOP_LEVEL_ITEMS.keys,
            )

            val navigator = rememberNavigator(
                state = navigationState,
                interceptors = emptyList(),
                onExit = ::finish
            )

            val entryProvider = remember(navigator) {
                entryProvider {
                    entryBuilders.forEach {
                        it(navigator)
                    }
                }
            }

            GeAlphTheme {
                AppRoot(
                    navigationState = navigationState,
                    navigator = navigator,
                    entryProvider = entryProvider,
                )
            }
        }
    }
}
