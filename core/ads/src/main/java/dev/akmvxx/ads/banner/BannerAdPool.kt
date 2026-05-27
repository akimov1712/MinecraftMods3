package dev.akmvxx.ads.banner

import android.content.Context
import com.cleversolutions.ads.AdError
import com.cleversolutions.ads.AdSize
import com.cleversolutions.ads.AdViewListener
import com.cleversolutions.ads.android.CASBannerView
import dev.akmvxx.ads.AdEvents
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

internal object BannerAdPool {

    private const val TYPE = "Banner"
    private const val TIMEOUT_MS = 20_000L
    private const val MAX_BACKOFF_EXP = 5

    private var poolSize = 5
    private val loaded = ArrayDeque<CASBannerView>()
    private val loading = mutableSetOf<CASBannerView>()

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

    fun clearListener() {
        preloadListener = null
    }

    fun startPreload() {
        if (!initialized) return
        fillPool()
    }

    fun hasAd(): Boolean = loaded.isNotEmpty()

    fun pop(): CASBannerView? {
        val view = loaded.removeFirstOrNull()
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

        val totalActive = loaded.size + loading.size
        val needToLoad = poolSize - totalActive

        repeat(needToLoad.coerceAtLeast(0)) { loadOne() }
    }

    private fun loadOne() {
        val id = placementId ?: return
        val context = appContext ?: return

        val width = context.resources.configuration.screenWidthDp
        val banner = CASBannerView(context, id)

        loading.add(banner)
        var completed = false

        val timeoutJob = scope.launch {
            delay(TIMEOUT_MS)
            if (!completed) {
                completed = true
                loading.remove(banner)
                banner.destroy()
                scheduleRetry()
            }
        }

        banner.adListener = object : AdViewListener {

            override fun onAdViewLoaded(view: CASBannerView) {
                if (completed) return
                completed = true
                timeoutJob.cancel()

                loading.remove(banner)
                loaded.add(view)
                retryAttempt = 0

                view.contentInfo?.let { AdEvents.loaded(TYPE, it) }
                notifyStatus()
                fillPool()
            }

            override fun onAdViewFailed(view: CASBannerView, error: AdError) {
                if (completed) return
                completed = true
                timeoutJob.cancel()

                loading.remove(banner)
                banner.destroy()

                AdEvents.failed("$TYPE load", error)
                scheduleRetry()
            }

            override fun onAdViewClicked(view: CASBannerView) {
                view.contentInfo?.let { AdEvents.clicked(TYPE, it) }
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
            if (loaded.size >= poolSize) PreloadStatus.PRELOADED
            else PreloadStatus.NOT_PRELOADED

        preloadListener?.invoke(status)
    }

    fun destroy() {
        retryJob?.cancel()
        scope.cancel()
        scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

        loaded.forEach { it.destroy() }
        loading.forEach { it.destroy() }

        loaded.clear()
        loading.clear()

        initialized = false
        retryAttempt = 0
        preloadListener = null
        appContext = null
        placementId = null
    }
}
