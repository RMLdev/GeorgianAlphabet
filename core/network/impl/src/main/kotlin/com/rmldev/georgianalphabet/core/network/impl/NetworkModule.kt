package com.rmldev.georgianalphabet.core.network.impl

import android.util.Log
import com.rmldev.georgianalphabet.core.network.api.Authenticated
import com.rmldev.georgianalphabet.core.network.api.Unauthenticated
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindsAuthenticator(djcAppAuthenticator: DjcAppAuthenticator): Authenticator

    companion object {

        @Provides
        @Singleton
        fun providesNetworkJson(): Json = Json {
            ignoreUnknownKeys = true
        }

        @Provides
        @Singleton
        fun providesBaseOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder().build()
        }

        @Provides
        @Singleton
        @Authenticated
        fun providesAuthenticatedOkHttpClient(
            base: dagger.Lazy<OkHttpClient>,
            authHeaderInterceptor: AuthHeaderInterceptor,
            authenticator: Authenticator,
        ): OkHttpClient {
            return base.get()
                .newBuilder()
                .addInterceptor(authHeaderInterceptor)
                .addDebugLogging()
                .authenticator(authenticator)
                .build()
        }

        @Provides
        @Singleton
        fun providesAuthenticatedRetrofit(
            @Authenticated okHttpClient: dagger.Lazy<OkHttpClient>,
            json: Json,
        ): Retrofit = createRetrofit(okHttpClient, json)

        @Provides
        @Singleton
        @Unauthenticated
        fun providesUnauthenticatedOkHttpClient(
            base: dagger.Lazy<OkHttpClient>,
        ): OkHttpClient {
            return base.get()
                .newBuilder()
                .addDebugLogging()
                .build()
        }

        @Provides
        @Singleton
        @Unauthenticated
        fun providesUnauthenticatedRetrofit(
            @Unauthenticated okHttpClient: dagger.Lazy<OkHttpClient>,
            json: Json,
        ): Retrofit = createRetrofit(okHttpClient, json)

        private fun OkHttpClient.Builder.addDebugLogging(): OkHttpClient.Builder = apply {
            if (!BuildConfig.DEBUG) return@apply

            addInterceptor(
                HttpLoggingInterceptor { Log.d("Network", it) }.apply {
                    level = HttpLoggingInterceptor.Level.BODY
                    redactHeader("Authorization")
                }
            )
        }

        private fun createRetrofit(
            okHttpClient: dagger.Lazy<OkHttpClient>,
            json: Json,
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .callFactory { okHttpClient.get().newCall(it) }
                .addConverterFactory(
                    json.asConverterFactory("application/json".toMediaType())
                )
                .build()
        }
    }
}
