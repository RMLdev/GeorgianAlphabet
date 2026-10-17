package com.rmldev.georgianalphabet.feature.preferences.impl.data

import androidx.datastore.core.DataStore
import com.rmldev.georgianalphabet.core.datastore.api.DataStoreProvider
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript
import com.rmldev.georgianalphabet.feature.preferences.api.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DataStoreUserPreferencesRepository @Inject constructor(
    private val dataStoreProvider: DataStoreProvider,
    private val localeScriptResolver: LocaleScriptResolver,
) : UserPreferencesRepository {

    override fun observe(): Flow<UserPreferences> = flow {
        store().data.collect { emit(it) }
    }
        .map { it.toDomain() }
        .distinctUntilChanged()

    override suspend fun completeOnboarding(knownScript: KnownScript?, userName: String?) {
        update {
            it.copy(
                onboardingCompleted = true,
                knownScript = knownScript ?: it.knownScript,
                userName = userName.normalizeName(),
            )
        }
    }

    override suspend fun setKnownScript(knownScript: KnownScript) {
        update { it.copy(knownScript = knownScript) }
    }

    override suspend fun setUserName(userName: String?) {
        update { it.copy(userName = userName.normalizeName()) }
    }

    private suspend fun update(transform: (StoredUserPreferences) -> StoredUserPreferences) {
        store().updateData(transform)
    }

    // Провайдер кеширует стор по имени файла, повторный вызов дешёвый
    private suspend fun store(): DataStore<StoredUserPreferences> = dataStoreProvider.provide(
        entityClazz = StoredUserPreferences::class,
        entitySerializer = StoredUserPreferencesSerializer,
        dataStoreFileName = FILE_NAME,
    )

    private fun StoredUserPreferences.toDomain() = UserPreferences(
        onboardingCompleted = onboardingCompleted,
        knownScript = knownScript ?: localeScriptResolver.defaultScript(),
        userName = userName,
    )

    private fun String?.normalizeName(): String? = this?.trim()?.takeIf { it.isNotEmpty() }

    private companion object {
        // Обычный (не зашифрованный) стор: настройки должны переживать смену устройства
        const val FILE_NAME = "user_preferences.json"
    }
}
