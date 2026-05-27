package dev.akmvxx.ads.nativead

import android.content.Context
import com.cleveradssolutions.sdk.AdContentInfo
import com.cleveradssolutions.sdk.nativead.AdChoicesPlacement
import com.cleveradssolutions.sdk.nativead.CASNativeLoader
import com.cleveradssolutions.sdk.nativead.NativeAdContent
import com.cleveradssolutions.sdk.nativead.NativeAdContentCallback
import com.cleversolutions.ads.AdError
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

internal object NativeAdPool {

    private const val TYPE = "Native"
    private const val REFILL_THRESHOLD = 2
    private const val LOAD_TIMEOUT_MS = 12_000L
    private const val MAX_PARALLEL_LOADS = 2
    private const val MAX_RETRY_BEFORE_RECREATE = 5
    private const val MAX_BACKOFF_EXP = 6

    private var appContext: Context? = null
    private var casId: String? = null
    private var poolSize = 5

    private var loader: CASNativeLoader? = null
    private val loaded = ArrayDeque<NativeAdContent>()

    private var initialized = false
    private var isDestroyed = false

    private var retryAttempt = 0
    private var loadingCount = 0
    private var loadStartedAt = 0L

    private var scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var retryJob: Job? = null

    private var onPreloadComplete: ((PreloadStatus) -> Unit)? = null

    fun setCallback(callback: (PreloadStatus) -> Unit) {
        onPreloadComplete = callback
    }

    fun clearCallback() {
        onPreloadComplete = null
    }

    fun init(context: Context, casId: String, poolSize: Int) {
        if (initialized) return

        this.appContext = context.applicationContext
        this.casId = casId
        this.poolSize = poolSize

        initialized = true
        isDestroyed = false
        retryAttempt = 0
        loadingCount = 0
        loadStartedAt = 0L

        createLoader()
    }

    fun load() {
        if (!initialized) return
        loadNext()
    }

    fun hasAd(): Boolean = loaded.isNotEmpty()

    fun pop(): NativeAdContent? {
        val ad = loaded.removeFirstOrNull()
        if (ad == null) {
            maybeRefill()
            return null
        }
        maybeRefill()
        return ad
    }

    private fun maybeRefill() {
        if (!initialized || isDestroyed) return
        if (loaded.size > REFILL_THRESHOLD) return
        loadNext()
    }

    private fun loadNext() {
        if (!initialized || isDestroyed) return
        if (loaded.size >= poolSize) return
        if (loadingCount >= MAX_PARALLEL_LOADS) return

        val now = System.currentTimeMillis()

        if (loader?.isLoading == true) {
            if (now - loadStartedAt > LOAD_TIMEOUT_MS) {
                recreateLoader()
            } else {
                return
            }
        }

        loadingCount++
        loadStartedAt = now
        loader?.load(1)
    }

    private fun recreateLoader() {
        loader = null
        createLoader()
        retryAttempt = 0
        loadingCount = 0
        loadStartedAt = 0L
    }

    private fun createLoader() {
        val context = appContext ?: return
        val id = casId ?: return

        loader = CASNativeLoader(context, id, nativeCallback).apply {
            adChoicesPlacement = AdChoicesPlacement.TOP_LEFT
            isStartVideoMuted = true
        }
    }

    private fun preloadStatus(): PreloadStatus =
        if (loaded.size >= poolSize) PreloadStatus.PRELOADED
        else PreloadStatus.NOT_PRELOADED

    private val nativeCallback = object : NativeAdContentCallback() {

        override fun onNativeAdLoaded(nativeAd: NativeAdContent, ad: AdContentInfo) {
            AdEvents.loaded(TYPE, ad)
            loadingCount = maxOf(0, loadingCount - 1)
            loadStartedAt = 0L
            retryAttempt = 0

            if (isDestroyed) {
                nativeAd.destroy()
                return
            }

            if (loaded.size < poolSize) {
                loaded.add(nativeAd)
            } else {
                nativeAd.destroy()
            }

            onPreloadComplete?.invoke(preloadStatus())
            loadNext()
        }

        override fun onNativeAdFailedToLoad(error: AdError) {
            AdEvents.failed("$TYPE load", error)
            loadingCount = maxOf(0, loadingCount - 1)
            loadStartedAt = 0L
            retryAttempt++

            if (retryAttempt >= MAX_RETRY_BEFORE_RECREATE) recreateLoader()

            val delayMs = 2.0.pow(min(retryAttempt, MAX_BACKOFF_EXP)).toLong() * 1000
            retryJob?.cancel()
            retryJob = scope.launch {
                delay(delayMs)
                loadNext()
            }
        }

        override fun onNativeAdFailedToShow(nativeAd: NativeAdContent, error: AdError) {
            AdEvents.failed("$TYPE show", error)
            nativeAd.destroy()
        }

        override fun onNativeAdClicked(nativeAd: NativeAdContent, ad: AdContentInfo) {
            AdEvents.clicked(TYPE, ad)
        }
    }

    fun destroy() {
        isDestroyed = true
        retryJob?.cancel()
        scope.cancel()
        scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

        loaded.forEach { it.destroy() }
        loaded.clear()

        loader = null
        onPreloadComplete = null

        initialized = false
        retryAttempt = 0
        loadingCount = 0
        loadStartedAt = 0L
    }
}
