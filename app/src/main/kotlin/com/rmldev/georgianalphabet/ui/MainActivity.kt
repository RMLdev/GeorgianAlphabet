package com.rmldev.georgianalphabet.ui

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
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
import com.rmldev.georgianalphabet.feature.preferences.api.usecase.ObserveUserPreferencesUseCase
import com.rmldev.georgianalphabet.navigation.TOP_LEVEL_ITEMS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.modules.SerializersModuleBuilder
import javax.inject.Inject

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {

    @Inject
    lateinit var entryBuilders: Set<@JvmSuppressWildcards EntryProviderScope<NavigationKey>.(Navigator) -> Unit>

    @Inject
    lateinit var keySerializerRegistrars: Set<@JvmSuppressWildcards SerializersModuleBuilder.() -> Unit>

    @Inject
    lateinit var observeUserPreferences: ObserveUserPreferencesUseCase

    // null, пока DataStore не прочитан
    private var onboardingCompleted: Boolean? by mutableStateOf(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            onboardingCompleted = observeUserPreferences().first().onboardingCompleted
        }
        keepSplashUntilReady()

        setContent {
            val completed = onboardingCompleted ?: return@setContent

            val navigationState = rememberNavigationState(
                isFirstLaunch = !completed,
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
                    val ready = onboardingCompleted != null
                    if (ready) content.viewTreeObserver.removeOnPreDrawListener(this)
                    return ready
                }
            },
        )
    }
}
