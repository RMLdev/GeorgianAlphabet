package com.rmldev.georgianalphabet.core.network.api

interface AuthTokenRefresher {
    suspend fun refresh(failedToken: String?): String?
}