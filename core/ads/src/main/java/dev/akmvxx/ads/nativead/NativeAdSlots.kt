package dev.akmvxx.ads.nativead

import android.content.Context
import com.cleveradssolutions.sdk.nativead.CASNativeView
import com.cleveradssolutions.sdk.nativead.NativeAdContent

/**
 * Caches an [NativeAdContent] + its bound [CASNativeView] per slot key so that
 * the same ad instance, view hierarchy and media player are reused across
 * recompositions and across LazyColumn item recycling.
 *
 * Without this cache every time a LazyColumn ad item leaves and re-enters the
 * viewport the composable would [NativeAdPool.pop] a fresh ad, inflate a new
 * View tree and restart the autoplay video — that was the root cause of the
 * scroll jank and the "video keeps replaying" symptom.
 */
internal object NativeAdSlots {

    internal class Holder(
        val ad: NativeAdContent,
        val views: NativeAdViews,
    )

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
