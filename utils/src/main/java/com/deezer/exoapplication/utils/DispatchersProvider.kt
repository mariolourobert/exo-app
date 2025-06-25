package com.deezer.exoapplication.utils

import kotlinx.coroutines.CoroutineDispatcher

open class DispatchersProvider(
    val main: CoroutineDispatcher,
    val default: CoroutineDispatcher,
    val io: CoroutineDispatcher,
    val immediate: CoroutineDispatcher,
)
