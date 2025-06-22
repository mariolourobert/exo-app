package com.deezer.exoapplication.domain.usecases

import com.deezer.exoapplication.domain.models.PlaylistWithTracksDomainModel
import com.deezer.exoapplication.utils.ResultOf
import kotlinx.coroutines.flow.Flow

interface GetPlaylistWithTracksUseCase {
    suspend operator fun invoke(playlistId: Int): Flow<ResultOf<PlaylistWithTracksDomainModel, GetPlaylistWithTracksError>>

    sealed interface GetPlaylistWithTracksError {
        data object PlaylistNotFound : GetPlaylistWithTracksError
        data object InvalidPlaylist : GetPlaylistWithTracksError
    }
}
