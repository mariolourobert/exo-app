package com.deezer.exoapplication.data.di

import com.deezer.exoapplication.data.mappers.PlaylistDataModelMapper
import com.deezer.exoapplication.data.mappers.TrackDataModelMapper
import com.deezer.exoapplication.data.repositories.PlaylistRepository
import com.deezer.exoapplication.data.repositories.PlaylistRepositoryImpl
import com.deezer.exoapplication.data.repositories.TrackRepository
import com.deezer.exoapplication.data.repositories.TrackRepositoryImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataKoinModule = module {
    factoryOf(::PlaylistDataModelMapper)
    factoryOf(::TrackDataModelMapper)
    factoryOf(::PlaylistRepositoryImpl) bind PlaylistRepository::class
    factoryOf(::TrackRepositoryImpl) bind TrackRepository::class
}
