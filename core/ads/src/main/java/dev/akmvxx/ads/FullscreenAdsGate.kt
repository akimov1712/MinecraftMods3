package dev.akmvxx.ads

internal object FullscreenAdsGate {

    private const val MIN_GAP_MS = 15_000L

    @Volatile
    private var lastShowAt = 0L

    fun canShow(): Boolean =
        System.currentTimeMillis() - lastShowAt >= MIN_GAP_MS

    fun markShown() {
        lastShowAt = System.currentTimeMillis()
    }
}
