package dev.akmvxx.ads.open

import android.app.Activity
import android.content.Context
import android.util.Log
import com.cleveradssolutions.sdk.AdContentInfo
import com.cleveradssolutions.sdk.AdFormat
import com.cleveradssolutions.sdk.screen.CASAppOpen
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

internal object OpenCasController {

    private const val TAG = "CAS_OPEN_AD"
    private const val MAX_BACKOFF_EXP = 6

    private var appOpenAd: CASAppOpen? = null

    private var initialized = false
    private var appContext: Context? = null
    private var state = AdState.IDLE

    private var retryAttempt = 0
    private var lastShowTime = 0L
    private var cooldownSeconds = 60

    private var scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var retryJob: Job? = null
    private var reloadJob: Job? = null

    private enum class AdState { IDLE, LOADING, READY, SHOWING }

    fun init(context: Context, casId: String, delay: Int) {
        if (initialized) return

        initialized = true
        appContext = context.applicationContext
        retryAttempt = 0
        cooldownSeconds = delay
        state = AdState.IDLE

        appOpenAd = CASAppOpen(casId).apply {
            contentCallback = this@OpenCasController.contentCallback
            isAutoloadEnabled = false
            isAutoshowEnabled = false
        }

        load()
    }

    private fun load() {
        if (!initialized) return
        val ad = appOpenAd ?: return
        val context = appContext ?: return

        if (state == AdState.LOADING) return
        if (ad.isLoaded) {
            state = AdState.READY
            return
        }

        state = AdState.LOADING
        ad.load(context)
    }

    fun show(activity: Activity) {
        if (!initialized) return
        val ad = appOpenAd ?: return
        if (!canShow()) return

        if (state == AdState.READY && ad.isLoaded) {
            state = AdState.SHOWING
            ad.show(activity)
        }
    }

    fun pause() {
        retryJob?.cancel()
        reloadJob?.cancel()
    }

    fun resume() {
        // No-op: lifecycle awareness without interfering scheduled loads
    }

    fun destroy() {
        retryJob?.cancel()
        reloadJob?.cancel()

        scope.cancel()
        scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

        appOpenAd?.destroy()
        appOpenAd = null

        initialized = false
        retryAttempt = 0
        state = AdState.IDLE
        appContext = null
        lastShowTime = 0L
    }

    private val contentCallback = object : ScreenAdContentCallback() {

        override fun onAdLoaded(ad: AdContentInfo) {
            Log.d(TAG, "loaded")
            retryAttempt = 0
            state = AdState.READY
        }

        override fun onAdFailedToLoad(format: AdFormat, error: AdError) {
            Log.d(TAG, "failed to load: ${error.message}")
            retryAttempt++
            state = AdState.IDLE
            scheduleRetry(calculateBackoff(retryAttempt))
        }

        override fun onAdShowed(ad: AdContentInfo) {
            Log.d(TAG, "showed")
            state = AdState.SHOWING
        }

        override fun onAdDismissed(ad: AdContentInfo) {
            Log.d(TAG, "dismissed")
            state = AdState.IDLE
            lastShowTime = System.currentTimeMillis()
            scheduleReloadAfterClose()
        }

        override fun onAdFailedToShow(format: AdFormat, error: AdError) {
            Log.d(TAG, "failed to show: ${error.message}")
            state = AdState.IDLE
            scheduleRetry(calculateBackoff(++retryAttempt))
        }

        override fun onAdClicked(ad: AdContentInfo) {
            Log.d(TAG, "clicked")
        }
    }

    private fun scheduleReloadAfterClose() {
        reloadJob?.cancel()
        val reloadDelay = if (cooldownSeconds > 10) 10L else cooldownSeconds.toLong()

        reloadJob = scope.launch {
            delay(reloadDelay * 1000L)
            if (initialized) load()
        }
    }

    private fun scheduleRetry(delayMs: Long) {
        retryJob?.cancel()
        retryJob = scope.launch {
            delay(delayMs)
            if (initialized) load()
        }
    }

    private fun canShow(): Boolean {
        val now = System.currentTimeMillis()
        return now - lastShowTime >= cooldownSeconds * 1000L
    }

    private fun calculateBackoff(attempt: Int): Long {
        val exp = min(MAX_BACKOFF_EXP, attempt)
        return (2.0.pow(exp.toDouble()) * 1000L).toLong()
    }
}
