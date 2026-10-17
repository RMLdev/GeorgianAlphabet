package com.rmldev.georgianalphabet.core.ui

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class StoreViewModelTest {

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `intent is mapped to action, actor event goes through reducer into ui state`() = runTest {
        val viewModel = TestViewModel()

        viewModel.dispatch(Intent.Increment)
        viewModel.dispatch(Intent.Increment)

        assertEquals("2", viewModel.uiState.value)
    }

    @Test
    fun `intent mapped to null is ignored`() = runTest {
        val viewModel = TestViewModel()

        viewModel.dispatch(Intent.Ignored)

        assertEquals("0", viewModel.uiState.value)
        assertEquals(emptyList(), viewModel.actor.executed)
    }

    @Test
    fun `initial actions are executed on creation`() = runTest {
        val viewModel = TestViewModel(initialActions = listOf(Action.Increment, Action.Increment))

        assertEquals("2", viewModel.uiState.value)
    }

    @Test
    fun `side effects are delivered to collector`() = runTest {
        val viewModel = TestViewModel()

        viewModel.sideEffects.test {
            viewModel.dispatch(Intent.Notify)
            assertEquals(SideEffect.Notified, awaitItem())
        }
    }

    @Test
    fun `side effects sent before collection are buffered`() = runTest {
        val viewModel = TestViewModel()

        viewModel.dispatch(Intent.Notify)

        viewModel.sideEffects.test {
            assertEquals(SideEffect.Notified, awaitItem())
        }
    }

    @Test
    fun `actor exception is passed to onError and does not break the store`() = runTest {
        val viewModel = TestViewModel()

        viewModel.dispatch(Intent.Fail)
        viewModel.dispatch(Intent.Increment)

        val (action, error) = requireNotNull(viewModel.lastError)
        assertEquals(Action.Fail, action)
        assertIs<IllegalStateException>(error)
        assertEquals("1", viewModel.uiState.value)
    }

    @Test
    fun `onError is not called for successful actions`() = runTest {
        val viewModel = TestViewModel()

        viewModel.dispatch(Intent.Increment)

        assertNull(viewModel.lastError)
    }

    private sealed interface Intent {
        data object Increment : Intent
        data object Notify : Intent
        data object Fail : Intent
        data object Ignored : Intent
    }

    private sealed interface Action {
        data object Increment : Action
        data object Notify : Action
        data object Fail : Action
    }

    private sealed interface Event {
        data object Incremented : Event
    }

    private sealed interface SideEffect {
        data object Notified : SideEffect
    }

    private data class State(val counter: Int = 0)

    private class TestActor : MviActor<Action, State, Event, SideEffect> {
        val executed = mutableListOf<Action>()

        override suspend fun ActorScope<State, Event, SideEffect>.executeAction(action: Action) {
            executed += action
            when (action) {
                Action.Increment -> event(Event.Incremented)
                Action.Notify -> sideEffect(SideEffect.Notified)
                Action.Fail -> error("boom")
            }
        }
    }

    private class TestViewModel(
        val actor: TestActor = TestActor(),
        initialActions: List<Action> = emptyList(),
    ) : StoreViewModel<Intent, Action, State, String, SideEffect, Event>(
        actor = actor,
        reducer = { state, event ->
            when (event) {
                Event.Incremented -> state.copy(counter = state.counter + 1)
            }
        },
        intentToAction = { intent ->
            when (intent) {
                Intent.Increment -> Action.Increment
                Intent.Notify -> Action.Notify
                Intent.Fail -> Action.Fail
                Intent.Ignored -> null
            }
        },
        initialState = State(),
        uiStateMapper = { it.counter.toString() },
        initialActions = initialActions,
    ) {
        var lastError: Pair<Action, Throwable>? = null

        override fun onError(action: Action, e: Throwable) {
            lastError = action to e
        }
    }
}
