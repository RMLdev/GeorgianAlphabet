package com.rmldev.georgianalphabet.feature.preferences.impl.data

import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript
import kotlinx.serialization.Serializable

/** Формат файла DataStore. Поля добавлять только с дефолтами: старые файлы должны читаться. */
@Serializable
internal data class StoredUserPreferences(
    val onboardingCompleted: Boolean = false,
    /** `null` — пользователь не выбирал, берём по локали. */
    val knownScript: KnownScript? = null,
    val userName: String? = null,
)
