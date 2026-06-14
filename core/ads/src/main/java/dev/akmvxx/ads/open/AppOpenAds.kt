package dev.akmvxx.ads.open

import android.app.Activity
import dev.akmvxx.ads.util.isShowNextAd
import dev.akmvxx.domain.entity.settings.SettingsEntity

object AppOpenAds {

    private var initialized = false
    private var showChance: Int = 100

    fun initialize(activity: Activity, settings: SettingsEntity) {
        if (initialized) return
        if (!settings.adEnabled.open) return

        initialized = true
        showChance = settings.adChangePercent.open

        AppOpenLoader.init(activity, settings.cooldownAdSecond)
    }

    fun pause() {
        if (initialized) AppOpenLoader.pause()
    }

    fun resume() {
        if (initialized) AppOpenLoader.resume()
    }

    fun show(activity: Activity) {
        if (!initialized) return
        if (!isShowNextAd(showChance)) return
        AppOpenLoader.show(activity)
    }

    fun destroy() {
        if (!initialized) return
        AppOpenLoader.destroy()
        initialized = false
    }
}
