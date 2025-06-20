package com.deezer.exoapplication.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "trackName") val trackName: String?,
    @ColumnInfo(name = "trackUri") val trackUri: String?,
)
