package com.deezer.exoapplication.data.repositories

import com.deezer.exoapplication.data.mappers.PlaylistDataModelMapper
import com.deezer.exoapplication.data.mappers.TrackDataModelMapper
import com.deezer.exoapplication.data.models.PlaylistWithTracksDataModel
import com.deezer.exoapplication.database.daos.PlaylistDao
import com.deezer.exoapplication.database.daos.PlaylistWithTracksDao
import com.deezer.exoapplication.database.entities.PlaylistTrackCrossRef
import com.deezer.exoapplication.utils.DispatchersProvider
import com.deezer.exoapplication.utils.TimeProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistWithTracksDao: PlaylistWithTracksDao,
    private val playlistDataModelMapper: PlaylistDataModelMapper,
    private val trackDataModelMapper: TrackDataModelMapper,
    private val dispatchersProvider: DispatchersProvider,
    private val timeProvider: TimeProvider,
) : PlaylistRepository {
    override suspend fun getPlaylistWithTracksAsFlow(playlistId: Int): Flow<PlaylistWithTracksDataModel?> {
        val playlist = playlistDao.getPlaylist(playlistId = playlistId)
            .let { playlistEntity ->
                playlistEntity?.let(playlistDataModelMapper::toDataModel)
            } ?: return flowOf(null)

        return playlistWithTracksDao.getPlaylistWithTracksAsFlow(playlistId = playlistId)
            .distinctUntilChanged()
            .map { playlistWithTracksQueryResult ->
                val tracks = playlistWithTracksQueryResult
                    .map { it.track }
                    .mapNotNull(trackDataModelMapper::toDataModel)

                return@map PlaylistWithTracksDataModel(
                    playlist = playlist,
                    tracks = tracks,
                )
            }
            .flowOn(dispatchersProvider.default)
    }

    override suspend fun getPlaylistWithTracks(playlistId: Int): PlaylistWithTracksDataModel? {
        val playlist = playlistDao.getPlaylist(playlistId = playlistId)
            .let { playlistEntity ->
                playlistEntity?.let(playlistDataModelMapper::toDataModel)
            } ?: return null

        val tracks = playlistWithTracksDao.getPlaylistWithTracks(playlistId = playlistId)
            .map { it.track }
            .mapNotNull(trackDataModelMapper::toDataModel)

        return PlaylistWithTracksDataModel(
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
                insertedAt = timeProvider.currentTimeMillis(),
            )
        ) != null
}
