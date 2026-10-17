package com.rmldev.georgianalphabet.core.ui

class ActionResult<Event : Any, SideEffect : Any>(
    val events: List<Event>,
    val sideEffects: List<SideEffect>
)

class ActionResultBuilder<Event : Any, SideEffect : Any>() {
    private val events: MutableList<Event> = mutableListOf()
    private val sideEffects: MutableList<SideEffect> = mutableListOf()

    fun build(): ActionResult<Event, SideEffect> = ActionResult(events, sideEffects)

    fun produceEvent(block: () -> Event) {
        events.add(block())
    }

    fun produceSideEffect(block: () -> SideEffect) {
        sideEffects.add(block())
    }
}