package com.deezer.exoapplication.data.repositories

import com.deezer.exoapplication.data.models.PlaylistWithTracksDataModel

interface PlaylistRepository {
    suspend fun getPlaylistWithTracks(playlistId: Int): PlaylistWithTracksDataModel?
}
