package com.rmldev.georgianalphabet.feature.preferences.impl.data

import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript
import com.rmldev.georgianalphabet.feature.preferences.api.model.UserPreferences
import kotlinx.coroutines.flow.Flow

internal interface UserPreferencesRepository {

    fun observe(): Flow<UserPreferences>

    suspend fun completeOnboarding(knownScript: KnownScript?, userName: String?)

    suspend fun setKnownScript(knownScript: KnownScript)

    suspend fun setUserName(userName: String?)
}
