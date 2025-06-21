package com.deezer.exoapplication.utils

import kotlinx.coroutines.CoroutineDispatcher

data class DispatchersProvider(
    val main: CoroutineDispatcher,
    val default: CoroutineDispatcher,
    val io: CoroutineDispatcher,
    val immediate: CoroutineDispatcher,
)
