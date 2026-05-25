package dev.akmvxx.ads.open

import android.app.Activity
import android.content.Context
import dev.akmvxx.ads.util.isShowNextAd
import dev.akmvxx.domain.entity.settings.SettingsEntity

object OpenCoordinator {

    private var initialized = false
    private var percentShow: Int = 100

    fun initialize(context: Context, casId: String, settings: SettingsEntity) {
        if (initialized) return
        if (!settings.adEnabled.open) return

        initialized = true
        percentShow = settings.adChangePercent.open

        OpenCasController.init(context.applicationContext, casId, settings.cooldownAdSecond)
    }

    fun pause() {
        if (!initialized) return
        OpenCasController.pause()
    }

    fun resume() {
        if (!initialized) return
        OpenCasController.resume()
    }

    fun show(activity: Activity) {
        if (!initialized) return
        if (!isShowNextAd(percentShow)) return
        OpenCasController.show(activity)
    }

    fun destroy() {
        if (!initialized) return
        OpenCasController.destroy()
        initialized = false
    }
}
