package com.deezer.exoapplication.app.di

import com.deezer.exoapplication.data.di.dataKoinModule
import com.deezer.exoapplication.database.di.databaseKoinModule
import com.deezer.exoapplication.utils.di.utilsKoinModule

internal val exoKoinModules =
    listOf(
        dataKoinModule,
        databaseKoinModule,
        utilsKoinModule,
    )
