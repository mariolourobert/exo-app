package com.deezer.exoapplication.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.deezer.exoapplication.database.queries.PlaylistWithTracksQueryResult

@Dao
interface PlaylistWithTracksDao {
    @Transaction
    @Query("SELECT * FROM Playlist WHERE playlistId = :playlistId")
    fun getPlaylistsWithSongs(playlistId: Int): List<PlaylistWithTracksQueryResult>
}
