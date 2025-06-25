package com.deezer.exoapplication.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.deezer.exoapplication.database.entities.PlaylistTrackCrossRef
import com.deezer.exoapplication.database.queries.PlaylistWithTracksQueryResult
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistWithTracksDao {
    @Transaction
    @Query(getPlaylistWithTracksQuery)
    fun getPlaylistWithTracksAsFlow(playlistId: Int): Flow<List<PlaylistWithTracksQueryResult>>

    @Transaction
    @Query(getPlaylistWithTracksQuery)
    suspend fun getPlaylistWithTracks(playlistId: Int): List<PlaylistWithTracksQueryResult>

    @Transaction
    @Query("DELETE FROM PlaylistTrackCrossRef WHERE trackId = :trackId AND playlistId = :playlistId")
    suspend fun removeTrackFromPlaylist(trackId: Int, playlistId: Int): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(playlistTrackCrossRef: PlaylistTrackCrossRef): Long?
}

private const val getPlaylistWithTracksQuery =
    """
        SELECT PlaylistTrackCrossRef.insertedAt,Track.trackId,Track.trackName,Track.trackUri
        FROM PlaylistTrackCrossRef
        INNER JOIN Track ON PlaylistTrackCrossRef.trackId = Track.trackId
        WHERE PlaylistTrackCrossRef.playlistId = :playlistId
        ORDER BY PlaylistTrackCrossRef.insertedAt
    """
