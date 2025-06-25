package com.deezer.exoapplication.domain.di

import com.deezer.exoapplication.domain.mappers.PlaylistDomainModelMapper
import com.deezer.exoapplication.domain.mappers.TrackDomainModelMapper
import com.deezer.exoapplication.domain.usecases.AddTrackToPlaylistUseCase
import com.deezer.exoapplication.domain.usecases.AddTrackToPlaylistUseCaseImpl
import com.deezer.exoapplication.domain.usecases.GetAllTracksUseCase
import com.deezer.exoapplication.domain.usecases.GetAllTracksUseCaseImpl
import com.deezer.exoapplication.domain.usecases.GetPlaylistWithTracksUseCase
import com.deezer.exoapplication.domain.usecases.GetPlaylistWithTracksUseCaseImpl
import com.deezer.exoapplication.domain.usecases.RemoveTrackFromPlaylistUseCase
import com.deezer.exoapplication.domain.usecases.RemoveTrackFromPlaylistUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val domainKoinModule = module {
    factoryOf(::PlaylistDomainModelMapper)
    factoryOf(::TrackDomainModelMapper)
    factoryOf(::GetPlaylistWithTracksUseCaseImpl) bind GetPlaylistWithTracksUseCase::class
    factoryOf(::RemoveTrackFromPlaylistUseCaseImpl) bind RemoveTrackFromPlaylistUseCase::class
    factoryOf(::GetAllTracksUseCaseImpl) bind GetAllTracksUseCase::class
    factoryOf(::AddTrackToPlaylistUseCaseImpl) bind AddTrackToPlaylistUseCase::class
}
