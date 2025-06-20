package com.deezer.exoapplication.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.deezer.exoapplication.database.entities.TrackEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(track: TrackEntity)

    @Query("SELECT * FROM tracks WHERE trackId = :trackId")
    suspend fun getTrack(trackId: Int): TrackEntity?
}
