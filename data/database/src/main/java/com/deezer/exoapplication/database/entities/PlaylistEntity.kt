package com.deezer.exoapplication.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
data class PlaylistEntity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "playlistName") val playlistName: String?,
)
