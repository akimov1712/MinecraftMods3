package dev.akmvxx.ads.open

import android.app.Activity
import com.yodo1.mas.appopenad.Yodo1MasAppOpenAd
import com.yodo1.mas.appopenad.Yodo1MasAppOpenAdListener
import com.yodo1.mas.appopenad.Yodo1MasAppOpenAdRevenueListener
import com.yodo1.mas.error.Yodo1MasError
import dev.akmvxx.ads.AdEvents
import dev.akmvxx.ads.FullscreenAdsGate
import java.lang.ref.WeakReference

internal object AppOpenLoader {

    private const val TYPE = "AppOpen"

    private var initialized = false
    private var activityRef: WeakReference<Activity>? = null

    private var cooldownSeconds = 60
    private var lastShowTime = 0L

    fun init(activity: Activity, cooldown: Int) {
        if (initialized) return
        initialized = true
        activityRef = WeakReference(activity)
        cooldownSeconds = cooldown

        Yodo1MasAppOpenAd.getInstance().setAdListener(listener)
        Yodo1MasAppOpenAd.getInstance().setAdRevenueListener(revenueListener)
        Yodo1MasAppOpenAd.getInstance().loadAd(activity)
    }

    fun show(activity: Activity) {
        if (!initialized) return
        activityRef = WeakReference(activity)
        if (!canShow()) return
        if (!FullscreenAdsGate.canShow()) return
        if (Yodo1MasAppOpenAd.getInstance().isLoaded) {
            FullscreenAdsGate.markShown()
            Yodo1MasAppOpenAd.getInstance().showAd(activity)
        }
    }

    fun pause() = Unit
    fun resume() = Unit

    fun destroy() {
        Yodo1MasAppOpenAd.getInstance().setAdListener(null)
        Yodo1MasAppOpenAd.getInstance().setAdRevenueListener(null)
        initialized = false
        cooldownSeconds = 60
        lastShowTime = 0L
        activityRef = null
    }

    private fun reload() {
        activityRef?.get()?.let { Yodo1MasAppOpenAd.getInstance().loadAd(it) }
    }

    private val listener = object : Yodo1MasAppOpenAdListener {
        override fun onAppOpenAdLoaded(ad: Yodo1MasAppOpenAd) {
            AdEvents.loaded(TYPE)
        }

        override fun onAppOpenAdFailedToLoad(
            ad: Yodo1MasAppOpenAd,
            error: Yodo1MasError,
        ) {
            AdEvents.failed("$TYPE load", error)
            reload()
        }

        override fun onAppOpenAdOpened(ad: Yodo1MasAppOpenAd) {

        }

        override fun onAppOpenAdFailedToOpen(
            ad: Yodo1MasAppOpenAd,
            error: Yodo1MasError,
        ) {
            AdEvents.failed("$TYPE show", error)
            reload()
        }

        override fun onAppOpenAdClosed(ad: Yodo1MasAppOpenAd) {
            AdEvents.dismissed(TYPE)
            lastShowTime = System.currentTimeMillis()
            reload()
        }
    }

    private val revenueListener = Yodo1MasAppOpenAdRevenueListener { _, value ->
        AdEvents.impression(TYPE, value)
    }

    private fun canShow(): Boolean =
        System.currentTimeMillis() - lastShowTime >= cooldownSeconds * 1000L
}
