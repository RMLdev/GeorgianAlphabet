package com.rmldev.georgianalphabet.core.navigation.api

sealed interface NavigationCommand {
    data class Push(val key: NavigationKey) : NavigationCommand
    data class Replace(val key: NavigationKey) : NavigationCommand
    data object Back : NavigationCommand
    class PopWhere(val predicate: (NavigationKey) -> Boolean) : NavigationCommand
    class TruncateEverywhere(val predicate: (NavigationKey) -> Boolean) : NavigationCommand
    data class Batch(val commands: List<NavigationCommand>) : NavigationCommand
    data object None : NavigationCommand
}