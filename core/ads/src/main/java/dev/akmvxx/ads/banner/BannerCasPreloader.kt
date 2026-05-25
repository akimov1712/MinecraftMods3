package dev.akmvxx.ads.banner

import android.content.Context
import android.util.Log
import com.cleversolutions.ads.AdError
import com.cleversolutions.ads.AdSize
import com.cleversolutions.ads.AdViewListener
import com.cleversolutions.ads.android.CASBannerView
import dev.akmvxx.ads.PreloadStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.math.pow

internal object BannerCasPreloader {

    private const val TAG = "CAS_BANNER_POOL"
    private const val TIMEOUT_MS = 20_000L
    private const val MAX_BACKOFF_EXP = 5

    private var poolSize = 5
    private val loadedViews = ArrayDeque<CASBannerView>()
    private val loadingViews = mutableSetOf<CASBannerView>()

    private var placementId: String? = null
    private var appContext: Context? = null

    private var initialized = false
    private var retryAttempt = 0

    private var scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var retryJob: Job? = null
    private var preloadListener: ((PreloadStatus) -> Unit)? = null

    fun initialize(context: Context, placementId: String, poolSize: Int) {
        if (initialized) return

        this.appContext = context.applicationContext
        this.placementId = placementId
        this.poolSize = poolSize
        initialized = true
    }

    fun setListener(listener: (PreloadStatus) -> Unit) {
        preloadListener = listener
        notifyStatus()
    }

    fun deleteListener() {
        preloadListener = null
    }

    fun startPreload() {
        if (!initialized) return
        fillPool()
    }

    fun hasAd(): Boolean = loadedViews.isNotEmpty()

    fun pop(): CASBannerView? {
        val view = loadedViews.removeFirstOrNull()
        if (view == null) {
            fillPool()
            notifyStatus()
            return null
        }

        fillPool()
        notifyStatus()
        return view
    }

    private fun fillPool() {
        if (!initialized) return

        val totalActive = loadedViews.size + loadingViews.size
        val needToLoad = poolSize - totalActive

        repeat(needToLoad.coerceAtLeast(0)) { loadOne() }
    }

    private fun loadOne() {
        val id = placementId ?: return
        val context = appContext ?: return

        val width = context.resources.configuration.screenWidthDp
        val banner = CASBannerView(context, id)

        loadingViews.add(banner)
        var completed = false

        val timeoutJob = scope.launch {
            delay(TIMEOUT_MS)
            if (!completed) {
                completed = true
                Log.d(TAG, "load timeout")
                loadingViews.remove(banner)
                banner.destroy()
                scheduleRetry()
            }
        }

        banner.adListener = object : AdViewListener {

            override fun onAdViewLoaded(view: CASBannerView) {
                if (completed) return
                completed = true
                timeoutJob.cancel()

                loadingViews.remove(banner)
                loadedViews.add(view)
                retryAttempt = 0

                Log.d(TAG, "loaded (${loadedViews.size}/$poolSize)")
                notifyStatus()
                fillPool()
            }

            override fun onAdViewFailed(view: CASBannerView, error: AdError) {
                if (completed) return
                completed = true
                timeoutJob.cancel()

                loadingViews.remove(banner)
                banner.destroy()

                Log.d(TAG, "failed to load: ${error.message}")
                scheduleRetry()
            }

            override fun onAdViewClicked(view: CASBannerView) {
                Log.d(TAG, "clicked")
            }
        }

        banner.size = AdSize.getInlineBanner(width, 350)
        banner.isAutoloadEnabled = false
        banner.load()
    }

    private fun scheduleRetry() {
        retryAttempt++
        val delayMs = 2.0.pow(min(retryAttempt, MAX_BACKOFF_EXP)).toLong() * 1000

        retryJob?.cancel()
        retryJob = scope.launch {
            delay(delayMs)
            fillPool()
        }

        notifyStatus()
    }

    private fun notifyStatus() {
        val status =
            if (loadedViews.size >= poolSize) PreloadStatus.PRELOADED
            else PreloadStatus.NOT_PRELOADED

        preloadListener?.invoke(status)
    }

    fun destroy() {
        retryJob?.cancel()
        scope.cancel()
        scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

        loadedViews.forEach { it.destroy() }
        loadingViews.forEach { it.destroy() }

        loadedViews.clear()
        loadingViews.clear()

        initialized = false
        retryAttempt = 0
        preloadListener = null
        appContext = null
        placementId = null
    }
}
