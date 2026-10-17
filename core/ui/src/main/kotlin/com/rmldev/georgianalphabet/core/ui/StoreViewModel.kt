package com.rmldev.georgianalphabet.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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

    open fun onError(action: Action, e: Throwable) = Unit

    protected fun <Item : Any> bindPaging(binder: PagingBinder<State, Item>): Flow<PagingData<Item>> {
        return binder.bind(mutableState, viewModelScope)
    }
}