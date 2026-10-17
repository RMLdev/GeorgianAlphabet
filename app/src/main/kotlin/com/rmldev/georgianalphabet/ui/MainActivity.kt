package com.rmldev.georgianalphabet.ui

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.entryProvider
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme
import com.rmldev.georgianalphabet.core.navigation.api.NavigationInterceptor
import com.rmldev.georgianalphabet.core.navigation.api.NavigationKey
import com.rmldev.georgianalphabet.core.navigation.api.Navigator
import com.rmldev.georgianalphabet.core.navigation.rememberNavigator
import com.rmldev.georgianalphabet.core.navigation.state.rememberNavigationState
import com.rmldev.georgianalphabet.feature.alphabet.api.AlphabetNavKey
import com.rmldev.georgianalphabet.feature.onboarding.api.OnboardingNavKey
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

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        keepSplashUntilReady()

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val ready = uiState as? MainUiState.Ready ?: return@setContent

            val navigationState = rememberNavigationState(
                isFirstLaunch = ready.showOnboarding,
                keySerializerRegistrars = keySerializerRegistrars,
                startTab = AlphabetNavKey,
                tabKeys = TOP_LEVEL_ITEMS.keys,
                startKey = OnboardingNavKey,
            )

            val navigator = rememberNavigator(
                state = navigationState,
                interceptors = emptyList(),
                onExit = ::finish,
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

    /**
     * Не рисуем первый кадр, пока не знаем стартовый экран: системный splash (API 31+)
     * или фон окна остаются на экране, и онбординг не мигает перед алфавитом.
     */
    private fun keepSplashUntilReady() {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    val ready = viewModel.uiState.value is MainUiState.Ready
                    if (ready) content.viewTreeObserver.removeOnPreDrawListener(this)
                    return ready
                }
            },
        )
    }
}
