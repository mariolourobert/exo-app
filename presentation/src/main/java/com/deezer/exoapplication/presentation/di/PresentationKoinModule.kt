package com.deezer.exoapplication.presentation.di

import com.deezer.exoapplication.presentation.library.LibraryScreenUiStateMapper
import com.deezer.exoapplication.presentation.library.LibraryScreenViewModel
import com.deezer.exoapplication.presentation.player.PlayerScreenUiStateMapper
import com.deezer.exoapplication.presentation.player.PlayerScreenViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationKoinModule = module {
    viewModelOf(::PlayerScreenViewModel)
    factoryOf(::PlayerScreenUiStateMapper)
    viewModelOf(::LibraryScreenViewModel)
    factoryOf(::LibraryScreenUiStateMapper)
}
