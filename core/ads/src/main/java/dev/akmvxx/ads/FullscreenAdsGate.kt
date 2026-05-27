package dev.akmvxx.ads

/**
 * Process-wide guard for any fullscreen ad (Interstitial / AppOpen).
 *
 * Google AdMob's [Ad placement policy](https://support.google.com/admob/answer/6128543)
 * forbids stacking multiple fullscreen ads back-to-back. Without this gate the
 * user could close an AppOpen ad, immediately tap a mod card and get an
 * Interstitial on the next frame — that triple-tap pattern is a fast track to
 * an account suspension.
 *
 * The gate is intentionally cheap: a single shared timestamp + a small
 * cooldown. Both [InterstitialAds] and [AppOpenAds] consult it before
 * showing and stamp it after a successful show / dismiss.
 */
internal object FullscreenAdsGate {

    /** Minimum gap between any two fullscreen ad impressions, milliseconds. */
    private const val MIN_GAP_MS = 15_000L

    @Volatile
    private var lastShowAt = 0L

    fun canShow(): Boolean =
        System.currentTimeMillis() - lastShowAt >= MIN_GAP_MS

    fun markShown() {
        lastShowAt = System.currentTimeMillis()
    }
}
