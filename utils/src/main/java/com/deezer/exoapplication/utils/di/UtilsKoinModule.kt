package com.deezer.exoapplication.utils.di

import com.deezer.exoapplication.utils.DispatchersProvider
import com.deezer.exoapplication.utils.TimeProvider
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val utilsKoinModule = module {
    single {
        DispatchersProvider(
            main = Dispatchers.Main,
            default = Dispatchers.Default,
            io = Dispatchers.IO,
            immediate = Dispatchers.Main.immediate,
        )
    }
    factoryOf(::TimeProvider)
}
