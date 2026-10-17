package com.rmldev.georgianalphabet.core.network.api

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Retention(RUNTIME)
@Qualifier
annotation class Authenticated

@Retention(RUNTIME)
@Qualifier
annotation class Unauthenticated