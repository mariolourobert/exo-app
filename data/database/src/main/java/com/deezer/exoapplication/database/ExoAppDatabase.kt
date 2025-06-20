package com.deezer.exoapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.deezer.exoapplication.database.daos.TrackDao
import com.deezer.exoapplication.database.entities.TrackEntity

@Database(
    entities = [
        TrackEntity::class,
    ],
    version = 1,
)
abstract class ExoAppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
}
