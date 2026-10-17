package com.rmldev.georgianalphabet.core.ui

import kotlinx.coroutines.CoroutineScope

interface ActorScope<out State : Any, in Event : Any, in SideEffect : Any> {
    val state: State
    val coroutineScope: CoroutineScope
    fun event(event: Event)
    suspend fun sideEffect(effect: SideEffect)
}

interface MviActor<in Action : Any, State : Any, Event : Any, SideEffect : Any> {
    suspend fun ActorScope<State, Event, SideEffect>.executeAction(action: Action)
}


