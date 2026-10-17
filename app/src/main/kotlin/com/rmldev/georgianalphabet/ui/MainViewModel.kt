package com.rmldev.georgianalphabet.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmldev.georgianalphabet.feature.preferences.api.usecase.ObserveUserPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Состояние запуска приложения: выбирает стартовый экран по сохранённым настройкам. */
@HiltViewModel
internal class MainViewModel @Inject constructor(private val observeUserPreferences: ObserveUserPreferencesUseCase) :
    ViewModel() {

    private val mutableUiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = mutableUiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Стартовый экран выбирается один раз: дальнейшие изменения настроек его не меняют
            val preferences = observeUserPreferences().first()
            mutableUiState.value = MainUiState.Ready(showOnboarding = !preferences.onboardingCompleted)
        }
    }
}

internal sealed interface MainUiState {
    /** DataStore ещё не прочитан, splash остаётся на экране. */
    data object Loading : MainUiState

    data class Ready(val showOnboarding: Boolean) : MainUiState
}
