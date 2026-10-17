package com.rmldev.georgianalphabet.core.ui

fun interface MviReducer<State : Any, in Event : Any> {
    fun reduce(oldState: State, event: Event): State
}