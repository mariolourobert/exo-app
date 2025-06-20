package com.deezer.exoapplication.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "playlistId")
    val uid: Int? = null,
    @ColumnInfo(name = "playlistName") val playlistName: String?,
)
