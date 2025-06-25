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
    @Query("SELECT * FROM Playlist WHERE playlistId = :playlistId")
    fun getPlaylistWithTracksAsFlow(playlistId: Int): Flow<PlaylistWithTracksQueryResult?>

    @Transaction
    @Query("SELECT * FROM Playlist WHERE playlistId = :playlistId")
    suspend fun getPlaylistWithTracks(playlistId: Int): PlaylistWithTracksQueryResult?

    @Transaction
    @Query("DELETE FROM PlaylistTrackCrossRef WHERE trackId = :trackId AND playlistId = :playlistId")
    suspend fun removeTrackFromPlaylist(trackId: Int, playlistId: Int): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(playlistTrackCrossRef: PlaylistTrackCrossRef): Long?
}
