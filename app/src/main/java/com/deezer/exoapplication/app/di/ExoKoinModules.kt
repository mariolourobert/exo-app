package com.deezer.exoapplication.app.di

import com.deezer.exoapplication.data.di.dataKoinModule
import com.deezer.exoapplication.database.di.databaseKoinModule
import com.deezer.exoapplication.domain.di.domainKoinModule
import com.deezer.exoapplication.presentation.di.presentationKoinModule
import com.deezer.exoapplication.utils.di.utilsKoinModule

internal val exoKoinModules =
    listOf(
        dataKoinModule,
        databaseKoinModule,
        domainKoinModule,
        presentationKoinModule,
        utilsKoinModule,
    )
