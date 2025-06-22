package com.deezer.exoapplication.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

fun <T, U> StateFlow<T>.mapStateFlow(
    coroutineScope: CoroutineScope,
    transform: (T) -> U,
): StateFlow<U> =
    map(transform)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = transform(value),
        )
