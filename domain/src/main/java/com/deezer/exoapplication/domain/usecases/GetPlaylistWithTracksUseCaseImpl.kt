package com.deezer.exoapplication.domain.usecases

import com.deezer.exoapplication.data.repositories.PlaylistRepository
import com.deezer.exoapplication.domain.mappers.PlaylistDomainModelMapper
import com.deezer.exoapplication.domain.mappers.TrackDomainModelMapper
import com.deezer.exoapplication.domain.models.PlaylistWithTracksDomainModel
import com.deezer.exoapplication.domain.usecases.GetPlaylistWithTracksUseCase.GetPlaylistWithTracksError
import com.deezer.exoapplication.utils.DispatchersProvider
import com.deezer.exoapplication.utils.ResultOf
import kotlinx.coroutines.withContext

internal class GetPlaylistWithTracksUseCaseImpl(
    private val playlistRepository: PlaylistRepository,
    private val playlistDomainModelMapper: PlaylistDomainModelMapper,
    private val trackDomainModelMapper: TrackDomainModelMapper,
    private val dispatchersProvider: DispatchersProvider,
) : GetPlaylistWithTracksUseCase {
    override suspend fun invoke(
        playlistId: Int,
    ): ResultOf<PlaylistWithTracksDomainModel, GetPlaylistWithTracksError> =
        withContext(dispatchersProvider.default) {
            val playlistWithTracksDataModel =
                playlistRepository.getPlaylistWithTracks(playlistId = playlistId)
                    ?: return@withContext ResultOf.failure(
                        GetPlaylistWithTracksError.PlaylistNotFound,
                    )

            val playlistDomainModel = playlistWithTracksDataModel.playlist
                .let(playlistDomainModelMapper::toDomainModel)
                ?: return@withContext ResultOf.failure(
                    GetPlaylistWithTracksError.InvalidPlaylist,
                )

            val tracksDomainModels = playlistWithTracksDataModel.tracks
                .mapNotNull(trackDomainModelMapper::toDomainModel)

            ResultOf.success(
                PlaylistWithTracksDomainModel(
                    playlist = playlistDomainModel,
                    tracks = tracksDomainModels,
                ),
            )
        }
}
