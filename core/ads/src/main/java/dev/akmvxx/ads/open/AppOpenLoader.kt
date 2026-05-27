package dev.akmvxx.ads.open

import android.app.Activity
import android.content.Context
import com.cleveradssolutions.sdk.AdContentInfo
import com.cleveradssolutions.sdk.AdFormat
import com.cleveradssolutions.sdk.OnAdImpressionListener
import com.cleveradssolutions.sdk.screen.CASAppOpen
import com.cleveradssolutions.sdk.screen.ScreenAdContentCallback
import com.cleversolutions.ads.AdError
import dev.akmvxx.ads.AdEvents

internal object AppOpenLoader {

    private const val TYPE = "AppOpen"

    private var appOpen: CASAppOpen? = null
    private var initialized = false

    private var cooldownSeconds = 60
    private var lastShowTime = 0L

    fun init(context: Context, casId: String, cooldown: Int) {
        if (initialized) return
        initialized = true
        cooldownSeconds = cooldown

        appOpen = CASAppOpen(casId).apply {
            contentCallback = callback
            // SDK auto-loads on init, retries on failure and reloads after
            // dismiss — best for fill rate. No manual retry loop needed.
            isAutoloadEnabled = true
            isAutoshowEnabled = false
            onImpressionListener = OnAdImpressionListener { ad ->
                AdEvents.impression(TYPE, ad)
            }
        }
        appOpen?.load(context.applicationContext)
    }

    fun show(activity: Activity) {
        if (!initialized) return
        val ad = appOpen ?: return
        if (!canShow()) return
        if (ad.isLoaded) ad.show(activity)
    }

    fun pause() = Unit
    fun resume() = Unit

    fun destroy() {
        appOpen?.destroy()
        appOpen = null
        initialized = false
        cooldownSeconds = 60
        lastShowTime = 0L
    }

    private val callback = object : ScreenAdContentCallback() {

        override fun onAdLoaded(ad: AdContentInfo) = AdEvents.loaded(TYPE, ad)

        override fun onAdShowed(ad: AdContentInfo) {
            // Impression logged via OnAdImpressionListener.
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

    private fun canShow(): Boolean =
        System.currentTimeMillis() - lastShowTime >= cooldownSeconds * 1000L
}
