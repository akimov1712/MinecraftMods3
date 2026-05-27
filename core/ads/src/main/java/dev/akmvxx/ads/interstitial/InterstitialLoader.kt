package dev.akmvxx.ads.interstitial

import android.app.Activity
import android.content.Context
import com.cleveradssolutions.sdk.AdContentInfo
import com.cleveradssolutions.sdk.AdFormat
import com.cleveradssolutions.sdk.OnAdImpressionListener
import com.cleveradssolutions.sdk.screen.CASInterstitial
import com.cleveradssolutions.sdk.screen.ScreenAdContentCallback
import com.cleversolutions.ads.AdError
import dev.akmvxx.ads.AdEvents
import dev.akmvxx.ads.FullscreenAdsGate

internal object InterstitialLoader {

    private const val TYPE = "Interstitial"

    private var interstitial: CASInterstitial? = null
    private var initialized = false

    private var cooldownSeconds = 60
    private var lastShowTime = 0L

    fun init(context: Context, casId: String, cooldown: Int) {
        if (initialized) return
        cooldownSeconds = cooldown
        initialized = true

        interstitial = CASInterstitial(casId).apply {
            contentCallback = callback
            // SDK handles initial load, retry on failure and reload after
            // dismiss on its own — that is the documented path for max
            // fill rate. Doing it ourselves doubles up requests.
            isAutoloadEnabled = true
            isAutoshowEnabled = false
            // We gate showing with our own cooldown driven by server
            // settings, so don't let CAS impose an extra interval.
            minInterval = 0
            onImpressionListener = OnAdImpressionListener { ad ->
                AdEvents.impression(TYPE, ad)
            }
        }
        interstitial?.load(context.applicationContext)
    }

    fun show(activity: Activity) {
        if (!initialized) return
        if (!canShow()) return
        if (!FullscreenAdsGate.canShow()) return
        val ad = interstitial ?: return
        if (ad.isLoaded) {
            FullscreenAdsGate.markShown()
            ad.show(activity)
        }
    }

    private fun canShow(): Boolean =
        System.currentTimeMillis() - lastShowTime >= cooldownSeconds * 1000L

    private val callback = object : ScreenAdContentCallback() {

        override fun onAdLoaded(ad: AdContentInfo) = AdEvents.loaded(TYPE, ad)

        override fun onAdShowed(ad: AdContentInfo) {
            // Impression is logged via OnAdImpressionListener — it fires
            // exactly when CAS counts the impression on the dashboard.
        }

        override fun onAdDismissed(ad: AdContentInfo) {
            AdEvents.dismissed(TYPE)
            lastShowTime = System.currentTimeMillis()
        }

        override fun onAdFailedToLoad(format: AdFormat, error: AdError) {
            AdEvents.failed("$TYPE load", error)
        }

        override fun onAdFailedToShow(format: AdFormat, error: AdError) {
            AdEvents.failed("$TYPE show", error)
        }

        override fun onAdClicked(ad: AdContentInfo) = AdEvents.clicked(TYPE, ad)
    }

    /**
     * Kept as a no-op so the public coordinator API and ProcessLifecycleOwner
     * wiring stay stable. Autoload manages its own pause/resume internally.
     */
    fun start() = Unit
    fun stop() = Unit

    fun destroy() {
        interstitial?.destroy()
        interstitial = null
        initialized = false
        cooldownSeconds = 60
        lastShowTime = 0L
    }
}
