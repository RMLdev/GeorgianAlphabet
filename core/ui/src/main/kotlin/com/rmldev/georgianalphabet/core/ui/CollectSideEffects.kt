package com.rmldev.georgianalphabet.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow

/**
 * Collects one-off store side effects while the screen is at least STARTED.
 * Effects sent in the background are buffered by [StoreViewModel.sideEffects] and
 * delivered when the screen comes back.
 */
@Composable
fun <T> CollectSideEffects(
    sideEffects: Flow<T>,
    onSideEffect: suspend (T) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnSideEffect by rememberUpdatedState(onSideEffect)

    LaunchedEffect(sideEffects, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            sideEffects.collect { currentOnSideEffect(it) }
        }
    }
}
