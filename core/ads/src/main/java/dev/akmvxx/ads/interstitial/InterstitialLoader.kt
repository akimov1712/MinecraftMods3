package dev.akmvxx.ads.interstitial

import android.app.Activity
import com.yodo1.mas.error.Yodo1MasError
import com.yodo1.mas.interstitial.Yodo1MasInterstitialAd
import com.yodo1.mas.interstitial.Yodo1MasInterstitialAdListener
import com.yodo1.mas.interstitial.Yodo1MasInterstitialAdRevenueListener
import dev.akmvxx.ads.AdEvents
import dev.akmvxx.ads.FullscreenAdsGate
import java.lang.ref.WeakReference

internal object InterstitialLoader {

    private const val TYPE = "Interstitial"

    private var initialized = false
    private var activityRef: WeakReference<Activity>? = null

    private var cooldownSeconds = 60
    private var lastShowTime = 0L

    fun init(activity: Activity, cooldown: Int) {
        if (initialized) return
        cooldownSeconds = cooldown
        initialized = true
        activityRef = WeakReference(activity)

        Yodo1MasInterstitialAd.getInstance().setAdListener(listener)
        Yodo1MasInterstitialAd.getInstance().setAdRevenueListener(revenueListener)
        Yodo1MasInterstitialAd.getInstance().loadAd(activity)
    }

    fun show(activity: Activity) {
        if (!initialized) return

        activityRef = WeakReference(activity)
        if (!canShow()) return
        if (!FullscreenAdsGate.canShow()) return
        if (Yodo1MasInterstitialAd.getInstance().isLoaded) {
            FullscreenAdsGate.markShown()
            Yodo1MasInterstitialAd.getInstance().showAd(activity)
        }
    }

    private fun canShow(): Boolean =
        System.currentTimeMillis() - lastShowTime >= cooldownSeconds * 1000L

    private fun reload() {
        activityRef?.get()?.let { Yodo1MasInterstitialAd.getInstance().loadAd(it) }
    }

    private val listener = object : Yodo1MasInterstitialAdListener {
        override fun onInterstitialAdLoaded(ad: Yodo1MasInterstitialAd) {
            AdEvents.loaded(TYPE)
        }

        override fun onInterstitialAdFailedToLoad(
            ad: Yodo1MasInterstitialAd,
            error: Yodo1MasError,
        ) {
            AdEvents.failed("$TYPE load", error)

            reload()
        }

        override fun onInterstitialAdOpened(ad: Yodo1MasInterstitialAd) {

        }

        override fun onInterstitialAdFailedToOpen(
            ad: Yodo1MasInterstitialAd,
            error: Yodo1MasError,
        ) {
            AdEvents.failed("$TYPE show", error)
            reload()
        }

        override fun onInterstitialAdClosed(ad: Yodo1MasInterstitialAd) {
            AdEvents.dismissed(TYPE)
            lastShowTime = System.currentTimeMillis()
            reload()
        }
    }

    private val revenueListener = Yodo1MasInterstitialAdRevenueListener { _, value ->
        AdEvents.impression(TYPE, value)
    }

    fun start() = Unit
    fun stop() = Unit

    fun destroy() {
        Yodo1MasInterstitialAd.getInstance().setAdListener(null)
        Yodo1MasInterstitialAd.getInstance().setAdRevenueListener(null)
        initialized = false
        cooldownSeconds = 60
        lastShowTime = 0L
        activityRef = null
    }
}
