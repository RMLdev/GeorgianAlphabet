package com.rmldev.georgianalphabet.feature.preferences.api.model

data class UserPreferences(
    val onboardingCompleted: Boolean,
    /** Выбор пользователя, а если он ещё не выбирал — значение по локали телефона. */
    val knownScript: KnownScript,
    val userName: String?,
)
