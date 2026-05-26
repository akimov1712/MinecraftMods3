package dev.akmvxx.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import dev.akmvxx.ads.AdsBootstrap
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject lateinit var adsBootstrap: AdsBootstrap

    override fun onCreate() {
        super.onCreate()
        setupCas(BuildConfig.APPLICATION_ID)
        adsBootstrap.start(BuildConfig.APPLICATION_ID)
    }
}
