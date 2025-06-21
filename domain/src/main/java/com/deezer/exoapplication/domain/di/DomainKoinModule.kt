package com.deezer.exoapplication.domain.di

import com.deezer.exoapplication.domain.mappers.PlaylistDomainModelMapper
import com.deezer.exoapplication.domain.mappers.TrackDomainModelMapper
import com.deezer.exoapplication.domain.usecases.GetPlaylistWithTracksUseCase
import com.deezer.exoapplication.domain.usecases.GetPlaylistWithTracksUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val domainKoinModule = module {
    factoryOf(::PlaylistDomainModelMapper)
    factoryOf(::TrackDomainModelMapper)
    factoryOf(::GetPlaylistWithTracksUseCaseImpl) bind GetPlaylistWithTracksUseCase::class
}
