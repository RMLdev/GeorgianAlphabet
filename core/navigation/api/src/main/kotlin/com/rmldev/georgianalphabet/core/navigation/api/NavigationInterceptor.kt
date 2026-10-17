package com.rmldev.georgianalphabet.core.navigation.api

interface NavigationInterceptor {
    fun intercept(command: NavigationCommand, context: NavigationContext): NavigationCommand
}