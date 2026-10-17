package com.rmldev.georgianalphabet.ui

import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript
import com.rmldev.georgianalphabet.feature.preferences.api.model.UserPreferences
import com.rmldev.georgianalphabet.feature.preferences.api.usecase.ObserveUserPreferencesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @Before
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @After
    fun tearDown() = Dispatchers.resetMain()

    private fun prefs(completed: Boolean) =
        UserPreferences(onboardingCompleted = completed, knownScript = KnownScript.LATIN, userName = null)

    private fun useCase(flow: Flow<UserPreferences>) = object : ObserveUserPreferencesUseCase {
        override fun invoke(): Flow<UserPreferences> = flow
    }

    @Test
    fun `loading until preferences are read`() = runTest {
        val flow = MutableSharedFlow<UserPreferences>()
        val viewModel = MainViewModel(useCase(flow))

        assertEquals(MainUiState.Loading, viewModel.uiState.value)

        flow.emit(prefs(completed = false))
        assertEquals(MainUiState.Ready(showOnboarding = true), viewModel.uiState.value)
    }

    @Test
    fun `completed onboarding starts on the alphabet and later changes are ignored`() = runTest {
        val flow = MutableStateFlow(prefs(completed = true))
        val viewModel = MainViewModel(useCase(flow))

        assertEquals(MainUiState.Ready(showOnboarding = false), viewModel.uiState.value)

        flow.value = prefs(completed = false)
        assertEquals(MainUiState.Ready(showOnboarding = false), viewModel.uiState.value)
    }
}
