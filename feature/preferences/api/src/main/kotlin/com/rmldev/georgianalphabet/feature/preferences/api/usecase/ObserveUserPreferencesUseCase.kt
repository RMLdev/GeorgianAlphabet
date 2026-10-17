package com.rmldev.georgianalphabet.feature.preferences.api.usecase

import com.rmldev.georgianalphabet.feature.preferences.api.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface ObserveUserPreferencesUseCase {
    operator fun invoke(): Flow<UserPreferences>
}
