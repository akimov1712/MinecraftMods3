package dev.akmvxx.ads.interstitial

import android.app.Activity
import android.content.Context
import dev.akmvxx.ads.util.isShowNextAd
import dev.akmvxx.domain.entity.settings.SettingsEntity

object InterstitialCoordinator {

    private var initialized = false
    private var percentShow: Int = 100

    fun initialize(context: Context, casId: String, settings: SettingsEntity) {
        if (initialized) return
        if (!settings.adEnabled.inter) return

        initialized = true
        percentShow = settings.adChangePercent.inter

        InterstitialCasController.init(context.applicationContext, casId, settings.cooldownAdSecond)
    }

    fun tryShow(activity: Activity) {
        if (!initialized) return
        if (!isShowNextAd(percentShow)) return
        InterstitialCasController.show(activity)
    }

    fun start() {
        if (!initialized) return
        InterstitialCasController.start()
    }

    fun stop() {
        if (!initialized) return
        InterstitialCasController.stop()
    }

    fun destroy() {
        if (!initialized) return
        InterstitialCasController.destroy()
        initialized = false
    }
}
