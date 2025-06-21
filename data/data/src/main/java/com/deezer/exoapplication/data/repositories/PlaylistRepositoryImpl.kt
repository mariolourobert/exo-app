package com.deezer.exoapplication.data.repositories

import com.deezer.exoapplication.data.mappers.PlaylistDataModelMapper
import com.deezer.exoapplication.data.mappers.TrackDataModelMapper
import com.deezer.exoapplication.data.models.PlaylistWithTracksDataModel
import com.deezer.exoapplication.database.daos.PlaylistWithTracksDao

internal class PlaylistRepositoryImpl(
    private val playlistWithTracksDao: PlaylistWithTracksDao,
    private val playlistDataModelMapper: PlaylistDataModelMapper,
    private val trackDataModelMapper: TrackDataModelMapper,
) : PlaylistRepository {
    override suspend fun getPlaylistWithTracks(playlistId: Int): PlaylistWithTracksDataModel? {
        val playlistWithTracksQueryResult =
            playlistWithTracksDao.getPlaylistWithTracks(playlistId = playlistId)
                ?: return null

        val playlist = playlistWithTracksQueryResult
            .playlist
            .let(playlistDataModelMapper::toDataModel)
            ?: return null

        val tracks = playlistWithTracksQueryResult
            .tracks
            .mapNotNull(trackDataModelMapper::toDataModel)

        return PlaylistWithTracksDataModel(
            playlist = playlist,
            tracks = tracks,
        )
    }
}
