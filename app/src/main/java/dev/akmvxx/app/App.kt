package dev.akmvxx.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        // Yodo1 MAS init happens later from MainActivity.onCreate because it
        // needs an Activity context for the built-in privacy dialog.
        setupMetrics()
    }
}
