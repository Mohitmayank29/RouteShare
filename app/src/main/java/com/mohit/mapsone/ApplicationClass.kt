package com.mohit.mapsone

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

//        PreferencesEncryptedShared.init(this@MyApp)
    }
}