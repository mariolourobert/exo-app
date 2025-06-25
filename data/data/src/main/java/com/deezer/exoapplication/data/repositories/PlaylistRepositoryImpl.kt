package com.deezer.exoapplication.data.repositories

import com.deezer.exoapplication.data.mappers.PlaylistDataModelMapper
import com.deezer.exoapplication.data.mappers.TrackDataModelMapper
import com.deezer.exoapplication.data.models.PlaylistWithTracksDataModel
import com.deezer.exoapplication.database.daos.PlaylistWithTracksDao
import com.deezer.exoapplication.database.entities.PlaylistTrackCrossRef
import com.deezer.exoapplication.utils.DispatchersProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class PlaylistRepositoryImpl(
    private val playlistWithTracksDao: PlaylistWithTracksDao,
    private val playlistDataModelMapper: PlaylistDataModelMapper,
    private val trackDataModelMapper: TrackDataModelMapper,
    private val dispatchersProvider: DispatchersProvider,
) : PlaylistRepository {
    override suspend fun getPlaylistWithTracksAsFlow(playlistId: Int): Flow<PlaylistWithTracksDataModel?> =
        playlistWithTracksDao.getPlaylistWithTracksAsFlow(playlistId = playlistId)
            .distinctUntilChanged()
            .map { playlistWithTracksQueryResult ->
                val playlist = playlistWithTracksQueryResult
                    ?.playlist
                    ?.let(playlistDataModelMapper::toDataModel)
                    ?: return@map null

                val tracks = playlistWithTracksQueryResult
                    .tracks
                    .mapNotNull(trackDataModelMapper::toDataModel)

                return@map PlaylistWithTracksDataModel(
                    playlist = playlist,
                    tracks = tracks,
                )
            }
            .flowOn(dispatchersProvider.default)

    override suspend fun getPlaylistWithTracks(playlistId: Int): PlaylistWithTracksDataModel? =
        playlistWithTracksDao.getPlaylistWithTracks(playlistId = playlistId)
            ?.let { playlistWithTracksQueryResult ->
                val playlist = playlistWithTracksQueryResult.playlist
                    .let(playlistDataModelMapper::toDataModel)
                    ?: return null

                val tracks = playlistWithTracksQueryResult.tracks
                    .mapNotNull(trackDataModelMapper::toDataModel)

                PlaylistWithTracksDataModel(
                    playlist = playlist,
                    tracks = tracks,
                )
            }

    override suspend fun removeTrackFromPlaylist(trackId: Int, playlistId: Int): Boolean =
        // playlistWithTracksDao.removeTrackFromPlaylist returns the number of rows deleted, it should be 1:
        playlistWithTracksDao.removeTrackFromPlaylist(trackId = trackId, playlistId = playlistId) == 1

    override suspend fun addTrackToPlaylist(trackId: Int, playlistId: Int): Boolean =
        playlistWithTracksDao.insert(
            playlistTrackCrossRef = PlaylistTrackCrossRef(
                trackId = trackId,
                playlistId = playlistId,
            )
        ) == 1
}
