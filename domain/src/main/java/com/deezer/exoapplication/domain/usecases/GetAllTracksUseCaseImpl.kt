package com.deezer.exoapplication.domain.usecases

import com.deezer.exoapplication.data.repositories.TrackRepository
import com.deezer.exoapplication.domain.mappers.TrackDomainModelMapper
import com.deezer.exoapplication.domain.models.TrackDomainModel
import com.deezer.exoapplication.utils.DispatchersProvider
import kotlinx.coroutines.withContext

class GetAllTracksUseCaseImpl(
    private val trackRepository: TrackRepository,
    private val trackDomainModelMapper: TrackDomainModelMapper,
    private val dispatchersProvider: DispatchersProvider,
) : GetAllTracksUseCase {
    override suspend fun invoke(): List<TrackDomainModel> =
        withContext(dispatchersProvider.default) {
            trackRepository.getAllTracks()
                .mapNotNull(trackDomainModelMapper::toDomainModel)
        }
}
