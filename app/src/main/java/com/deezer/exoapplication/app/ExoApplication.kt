package com.deezer.exoapplication.app

import android.app.Application
import com.deezer.exoapplication.app.di.startExoKoin

class ExoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startExoKoin()
    }
}
