package com.deezer.exoapplication.database.di

import androidx.room.Room
import com.deezer.exoapplication.database.ExoAppDatabase
import com.deezer.exoapplication.database.daos.PlaylistDao
import com.deezer.exoapplication.database.daos.PlaylistWithTracksDao
import com.deezer.exoapplication.database.daos.TrackDao
import com.deezer.exoapplication.database.debug.PopulateDatabaseManager
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val databaseKoinModule = module {
    single<ExoAppDatabase> {
        Room.databaseBuilder(
            context = get(),
            klass = ExoAppDatabase::class.java,
            name = "exo-app-database",
        ).build()
    }
    factory<TrackDao> {
        get<ExoAppDatabase>().trackDao()
    }
    factory<PlaylistDao> {
        get<ExoAppDatabase>().playlistDao()
    }
    factory<PlaylistWithTracksDao> {
        get<ExoAppDatabase>().playlistWithTracksDao()
    }
    factoryOf(::PopulateDatabaseManager)
}
