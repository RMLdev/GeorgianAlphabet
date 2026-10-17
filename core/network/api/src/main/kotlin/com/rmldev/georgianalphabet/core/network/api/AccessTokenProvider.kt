package com.rmldev.georgianalphabet.core.network.api

interface AccessTokenProvider {

   suspend fun getAccessToken(): String?
}