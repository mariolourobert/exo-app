package com.deezer.exoapplication.testUtils

import com.deezer.exoapplication.utils.DispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher

class TestDispatchersProvider(
    testDispatcher: CoroutineDispatcher = StandardTestDispatcher(),
) : DispatchersProvider(
    main = testDispatcher,
    default = testDispatcher,
    io = testDispatcher,
    immediate = testDispatcher,
)
