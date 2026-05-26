package dev.akmvxx.ads.banner

import com.cleversolutions.ads.android.CASBannerView

/**
 * Caches a [CASBannerView] per slot key. Without it a LazyColumn that brings
 * an ad slot back into view would pop a new banner from the preloader, drain
 * the pool faster than it can refill and rebuild the WebView on every scroll —
 * which is heavy enough to drop frames.
 */
internal object BannerAdSlots {

    private val cache = mutableMapOf<String, CASBannerView>()

    fun acquire(key: String): CASBannerView? {
        cache[key]?.let { return it }

        val view = BannerAdPool.pop() ?: return null
        cache[key] = view
        return view
    }

    fun releaseAll() {
        cache.values.forEach { view -> runCatching { view.destroy() } }
        cache.clear()
    }
}
