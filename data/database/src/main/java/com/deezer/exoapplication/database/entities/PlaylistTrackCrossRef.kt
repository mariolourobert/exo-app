package com.deezer.exoapplication.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    primaryKeys = ["playlistId", "trackId"],
)
data class PlaylistTrackCrossRef(
    val playlistId: Int,
    val trackId: Int,
    @ColumnInfo(name = "insertedAt")
    val insertedAt: Long,
)
