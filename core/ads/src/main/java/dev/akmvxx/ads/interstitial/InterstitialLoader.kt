package dev.akmvxx.ads.interstitial

import android.app.Activity
import android.content.Context
import android.util.Log
import com.cleveradssolutions.sdk.AdContentInfo
import com.cleveradssolutions.sdk.AdFormat
import com.cleveradssolutions.sdk.screen.CASInterstitial
import com.cleveradssolutions.sdk.screen.ScreenAdContentCallback
import com.cleversolutions.ads.AdError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.math.pow

internal object InterstitialLoader {

    private const val TAG = "InterstitialLoader"
    private const val MAX_BACKOFF_EXP = 5

    private var interstitial: CASInterstitial? = null
    private var appContext: Context? = null

    private var initialized = false
    private var isLoading = false
    private var paused = false

    private var retryAttempt = 0
    private var retryJob: Job? = null
    private var reloadJob: Job? = null

    private var scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private var cooldownSeconds = 60
    private var lastShowTime = 0L

    fun init(context: Context, casId: String, cooldown: Int) {
        if (initialized) return

        cooldownSeconds = cooldown
        appContext = context.applicationContext
        initialized = true

        interstitial = CASInterstitial(casId).apply {
            contentCallback = callback
            isAutoloadEnabled = false
            isAutoshowEnabled = false
        }

        load()
    }

    fun show(activity: Activity) {
        if (!initialized || paused) return
        if (!canShow()) return

        val ad = interstitial ?: return
        if (ad.isLoaded) ad.show(activity)
    }

    private fun canShow(): Boolean {
        val now = System.currentTimeMillis()
        return now - lastShowTime >= cooldownSeconds * 1000L
    }

    private fun baseReloadDelaySeconds(): Long =
        if (cooldownSeconds > 10) 10L else cooldownSeconds.toLong()

    private fun scheduleLoadWithBaseDelay() {
        reloadJob?.cancel()
        val delaySec = baseReloadDelaySeconds()

        reloadJob = scope.launch {
            delay(delaySec * 1000L)
            if (!paused && initialized) load()
        }
    }

    private fun load() {
        if (!initialized || paused || isLoading) return
        val context = appContext ?: return

        isLoading = true
        interstitial?.load(context)
    }

    private val callback = object : ScreenAdContentCallback() {

        override fun onAdLoaded(ad: AdContentInfo) {
            Log.d(TAG, "loaded")
            retryAttempt = 0
            retryJob?.cancel()
            isLoading = false
        }

        override fun onAdFailedToLoad(format: AdFormat, error: AdError) {
            Log.d(TAG, "failed to load: ${error.message}")
            isLoading = false
            scheduleRetry()
        }

        override fun onAdShowed(ad: AdContentInfo) {
            Log.d(TAG, "showed")
        }

        override fun onAdDismissed(ad: AdContentInfo) {
            Log.d(TAG, "dismissed")
            lastShowTime = System.currentTimeMillis()
            scheduleLoadWithBaseDelay()
        }

        override fun onAdFailedToShow(format: AdFormat, error: AdError) {
            Log.d(TAG, "failed to show: ${error.message}")
            scheduleRetry()
        }

        override fun onAdClicked(ad: AdContentInfo) {
            Log.d(TAG, "clicked")
        }
    }

    private fun scheduleRetry() {
        retryAttempt++
        val delayMs = 2.0.pow(min(retryAttempt, MAX_BACKOFF_EXP)).toLong() * 1000

        retryJob?.cancel()
        retryJob = scope.launch {
            delay(delayMs)
            if (!paused && initialized) load()
        }
    }

    fun stop() {
        paused = true
    }

    fun start() {
        paused = false
        if (interstitial?.isLoaded != true) scheduleLoadWithBaseDelay()
    }

    fun destroy() {
        retryJob?.cancel()
        reloadJob?.cancel()

        scope.cancel()
        scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

        interstitial?.destroy()
        interstitial = null

        retryAttempt = 0
        isLoading = false
        initialized = false
        paused = false
        appContext = null
        lastShowTime = 0L
    }
}
