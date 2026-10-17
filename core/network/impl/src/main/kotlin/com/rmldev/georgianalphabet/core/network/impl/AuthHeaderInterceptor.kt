package com.rmldev.georgianalphabet.core.network.impl

import com.rmldev.georgianalphabet.core.network.api.AccessTokenProvider
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal class AuthHeaderInterceptor @Inject constructor(
    private val accessTokenProvider: AccessTokenProvider,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { accessTokenProvider.getAccessToken() }
            ?: return chain.proceed(
                request = chain.request()
            )

        return chain.proceed(
            request = chain
                .request()
                .newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        )
    }
}