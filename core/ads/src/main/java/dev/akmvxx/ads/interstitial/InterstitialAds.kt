package dev.akmvxx.ads.interstitial

import android.app.Activity
import dev.akmvxx.ads.util.isShowNextAd
import dev.akmvxx.domain.entity.settings.SettingsEntity

object InterstitialAds {

    private var initialized = false
    private var showChance: Int = 100

    fun initialize(activity: Activity, settings: SettingsEntity) {
        if (initialized) return
        if (!settings.adEnabled.inter) return

        initialized = true
        showChance = settings.adChangePercent.inter

        InterstitialLoader.init(activity, settings.cooldownAdSecond)
    }

    fun tryShow(activity: Activity) {
        if (!initialized) return
        if (!isShowNextAd(showChance)) return
        InterstitialLoader.show(activity)
    }

    fun start() {
        if (initialized) InterstitialLoader.start()
    }

    fun stop() {
        if (initialized) InterstitialLoader.stop()
    }

    fun destroy() {
        if (!initialized) return
        InterstitialLoader.destroy()
        initialized = false
    }
}
