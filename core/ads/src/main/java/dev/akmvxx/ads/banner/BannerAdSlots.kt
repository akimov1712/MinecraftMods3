package dev.akmvxx.ads.banner

import com.cleversolutions.ads.android.CASBannerView

/**
 * Caches a [CASBannerView] per slot key. Without it a LazyColumn that brings
 * an ad slot back into view would pop a new banner from the preloader, drain
 * the pool faster than it can refill and rebuild the WebView on every scroll —
 * which is heavy enough to drop frames.
 *
 * On acquire the cached banner is switched into autoload mode so CAS keeps
 * rotating creatives on its own refresh interval while the slot stays
 * attached, which gives us extra impressions (and revenue) per session.
 */
internal object BannerAdSlots {

    private val cache = mutableMapOf<String, CASBannerView>()

    fun acquire(key: String): CASBannerView? {
        cache[key]?.let { return it }

        val view = BannerAdPool.pop() ?: return null
        // Pooled banners stay frozen so the pool can ration them — once a
        // banner is bound to a real slot, enable autoload so CAS refreshes
        // it in place. Refresh interval is set globally via
        // CAS.settings.bannerRefreshInterval.
        view.isAutoloadEnabled = true
        cache[key] = view
        return view
    }

    fun releaseAll() {
        cache.values.forEach { view -> runCatching { view.destroy() } }
        cache.clear()
    }
}
