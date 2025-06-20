package com.deezer.exoapplication.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.deezer.exoapplication.database.entities.PlaylistEntity

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(track: PlaylistEntity)

    @Query("SELECT * FROM playlist WHERE uid = :playlistId")
    suspend fun getPlaylist(playlistId: Int): PlaylistEntity?
}
