package com.rmldev.georgianalphabet.core.ui

import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

fun interface PagingBinder<State : Any, Item : Any> {
    fun bind(state: Flow<State>, scope: CoroutineScope): Flow<PagingData<Item>>
}