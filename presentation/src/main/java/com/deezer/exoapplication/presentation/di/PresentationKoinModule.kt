package com.deezer.exoapplication.presentation.di

import com.deezer.exoapplication.presentation.player.PlayerScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationKoinModule = module {
    viewModelOf(::PlayerScreenViewModel)
}
