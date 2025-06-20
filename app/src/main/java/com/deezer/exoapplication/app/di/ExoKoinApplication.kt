package com.deezer.exoapplication.app.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

internal fun Application.startExoKoin() {
    startKoin {
        androidContext(this@startExoKoin)
        modules(exoKoinModules)
    }
}
