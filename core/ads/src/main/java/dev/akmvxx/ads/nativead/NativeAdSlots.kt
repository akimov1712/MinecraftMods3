package dev.akmvxx.ads.nativead

import android.content.Context
import com.cleveradssolutions.sdk.nativead.NativeAdContent

internal object NativeAdSlots {

    /**
     * Holds the CAS native ad and its bound view tree for a single slot. The
     * [bound] flag guards against double-binding the same NativeAdContent
     * during fast scroll, when a LazyColumn slot can detach and reattach
     * before the previous `doOnAttach` callback fires.
     */
    internal class Holder(
        val ad: NativeAdContent,
        val views: NativeAdViews,
    ) {
        var bound: Boolean = false
    }

    private val cache = mutableMapOf<String, Holder>()

    fun acquireInline(context: Context, key: String): Holder? = acquire(key) {
        buildInlineNativeAdView(context)
    }

    fun acquireFullscreen(context: Context, key: String): Holder? = acquire(key) {
        buildFullscreenNativeAdView(context)
    }

    private inline fun acquire(key: String, buildViews: () -> NativeAdViews): Holder? {
        cache[key]?.let { return it }

        val ad = NativeAdPool.pop() ?: return null
        val views = buildViews().also { it.bindToRoot() }
        val holder = Holder(ad, views)
        cache[key] = holder
        return holder
    }

    fun releaseAll() {
        cache.values.forEach { holder ->
            runCatching { holder.ad.destroy() }
        }
        cache.clear()
    }
}
