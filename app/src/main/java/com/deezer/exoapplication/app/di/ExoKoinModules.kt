package com.deezer.exoapplication.app.di

import com.deezer.exoapplication.database.di.databaseKoinModule

internal val exoKoinModules =
    listOf(
        databaseKoinModule,
    )
