package com.rmldev.georgianalphabet.core.navigation.api

interface Navigator {
    fun navigateTo(key: NavigationKey)
    fun popWhere(predicate: (NavigationKey) -> Boolean)
    fun truncateEverywhere(predicate: (NavigationKey) -> Boolean)
    fun back()
}