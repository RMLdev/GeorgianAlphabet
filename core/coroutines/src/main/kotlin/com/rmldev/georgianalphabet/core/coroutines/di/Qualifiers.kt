package com.rmldev.georgianalphabet.core.coroutines.di

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Retention(RUNTIME)
@Qualifier
annotation class ApplicationScope

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val dispatcher: GeAlphDispatchers)

enum class GeAlphDispatchers {
    Default,
    IO,
}
