package com.rmldev.georgianalphabet.core.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

abstract class StoreViewModel<Intent : Any, Action : Any, State : Any, UiState : Any, SideEffect : Any, Event : Any>(
    private val actor: MviActor<Action, State, Event, SideEffect>,
    private val reducer: MviReducer<State, Event>,
    private val intentToAction: (Intent) -> Action?,
    initialState: State,
    uiStateMapper: (State) -> UiState,
    initialActions: List<Action> = emptyList(),
) : ViewModel() {

    private val mutableState: MutableStateFlow<State> = MutableStateFlow(initialState)

    /** Current store state, e.g. to mirror it into SavedStateHandle. Change it only through the actor. */
    protected val state: StateFlow<State> = mutableState.asStateFlow()

    private val sideEffectsChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)

    val sideEffects: Flow<SideEffect>
        get() = sideEffectsChannel.receiveAsFlow()

    val uiState: StateFlow<UiState> = mutableState
        .map(uiStateMapper)
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Eagerly, uiStateMapper(initialState))

    private val actorScope = object : ActorScope<State, Event, SideEffect> {
        override val state: State get() = mutableState.value
        override val coroutineScope: CoroutineScope get() = viewModelScope
        override fun event(event: Event) = mutableState.update { reducer.reduce(it, event) }
        override suspend fun sideEffect(effect: SideEffect) = sideEffectsChannel.send(effect)
    }

    init {
        viewModelScope.launch {
            initialActions.forEach { processAction(it) }
        }
    }

    fun dispatch(intent: Intent) {
        viewModelScope.launch {
            intentToAction(intent)?.let { processAction(it) }
        }
    }

    private suspend fun processAction(action: Action) {
        try {
            with(actor) { actorScope.executeAction(action) }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            onError(action, e)
        }
    }

    /**
     * Error thrown by [MviActor]. Logged by default; override to show it to the user
     * (via an event or a side effect).
     */
    open fun onError(action: Action, e: Throwable) {
        Log.e(this::class.simpleName, "Failed to execute $action", e)
    }
}
