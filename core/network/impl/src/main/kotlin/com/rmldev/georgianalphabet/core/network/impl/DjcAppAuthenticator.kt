package com.rmldev.georgianalphabet.core.network.impl

import com.rmldev.georgianalphabet.core.network.api.AuthTokenRefresher
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider

internal class DjcAppAuthenticator @Inject constructor(
    private val tokenRefresher: Provider<AuthTokenRefresher>,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.priorResponse != null) return null
        val failedToken = response.request.header("Authorization")?.removePrefix("Bearer ")
        val newToken = runBlocking {
            tokenRefresher.get().refresh(failedToken = failedToken)
        } ?: return null

        return response.request.newBuilder()
            .header("Authorization", "Bearer $newToken")
            .build()
    }
}