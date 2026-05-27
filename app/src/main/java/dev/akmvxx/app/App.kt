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
        setupMetrics()
        // AdsBootstrap drives the rest: fetches server settings first, then
        // calls setupCas (off main thread) with the ad types the server
        // actually enabled, and finally initializes the public ad facades.
        adsBootstrap.start(BuildConfig.APPLICATION_ID) { casId, enabled ->
            setupCas(casId, enabled)
        }
    }
}
