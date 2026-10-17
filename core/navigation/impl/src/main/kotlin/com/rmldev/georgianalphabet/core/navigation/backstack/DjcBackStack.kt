package com.rmldev.georgianalphabet.core.navigation.backstack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.StateObject
import androidx.savedstate.serialization.SavedStateConfiguration
import com.rmldev.georgianalphabet.core.navigation.api.BackStackEntry
import com.rmldev.georgianalphabet.core.navigation.api.NavigationKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer

@Serializable(with = DjsBackStackSerializer::class)
class DjcBackStack<T : NavigationKey>(val base: SnapshotStateList<BackStackEntry<T>>) :
    MutableList<BackStackEntry<T>> by base, StateObject by base, RandomAccess by base {

    fun resetToRoot() {
        check(isNotEmpty()) { "Back stack is empty" }
        if (size > 1) subList(1, size).clear()
    }

    constructor(vararg elements: T) : this(mutableStateListOf(*elements.map { BackStackEntry.new(it) }
        .toTypedArray()))
}

@Composable
fun rememberDjcNavBackStack(
    configuration: SavedStateConfiguration,
    vararg elements: NavigationKey,
): DjcBackStack<NavigationKey> {
    return rememberSerializable(
        serializer = serializer(),
        configuration = configuration,
    ) {
        DjcBackStack(*elements)
    }
}
