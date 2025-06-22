package com.deezer.exoapplication.data.repositories

import com.deezer.exoapplication.data.models.PlaylistWithTracksDataModel
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun getPlaylistWithTracks(playlistId: Int): Flow<PlaylistWithTracksDataModel?>
}
