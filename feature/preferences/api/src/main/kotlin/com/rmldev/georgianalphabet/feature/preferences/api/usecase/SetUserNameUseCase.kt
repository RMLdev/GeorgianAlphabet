package com.rmldev.georgianalphabet.feature.preferences.api.usecase

interface SetUserNameUseCase {
    /** Имя обрезается, пустое сохраняется как `null`. */
    suspend operator fun invoke(userName: String?)
}
