package com.deezer.exoapplication.data.repositories

import com.deezer.exoapplication.data.models.PlaylistWithTracksDataModel
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun getPlaylistWithTracksAsFlow(playlistId: Int): Flow<PlaylistWithTracksDataModel?>
    suspend fun getPlaylistWithTracks(playlistId: Int): PlaylistWithTracksDataModel?
    suspend fun removeTrackFromPlaylist(trackId: Int, playlistId: Int): Boolean
    suspend fun addTrackToPlaylist(trackId: Int, playlistId: Int): Boolean
}
