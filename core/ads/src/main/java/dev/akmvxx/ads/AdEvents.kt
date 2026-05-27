package dev.akmvxx.ads

import android.util.Log
import com.cleveradssolutions.sdk.AdContentInfo
import com.cleversolutions.ads.AdError

/**
 * Single logging channel for ad telemetry. Each event prints the network
 * source, eCPM (USD) and revenue precision so the logcat is enough to spot
 * which networks are filling, which are silent and what they pay per
 * impression — useful both for tuning the CAS waterfall and for sanity
 * checking against the CAS analytics dashboard.
 *
 * Tag is always "Ads" — easy to filter:
 *   adb logcat -s Ads
 */
internal object AdEvents {

    private const val TAG = "Ads"

    fun loaded(type: String, ad: AdContentInfo) {
        Log.d(TAG, "$type loaded · ${ad.describe()}")
    }

    fun impression(type: String, ad: AdContentInfo) {
        Log.d(
            TAG,
            "$type IMPRESSION · ${ad.describe()} · totalRevenue=${formatRevenue(ad.revenueTotal)}",
        )
    }

    fun clicked(type: String, ad: AdContentInfo) {
        Log.d(TAG, "$type clicked · src=${ad.sourceName}")
    }

    fun dismissed(type: String) {
        Log.d(TAG, "$type dismissed")
    }

    fun failed(stage: String, error: AdError) {
        Log.d(TAG, "$stage FAILED · code=${error.code} · ${error.message}")
    }

    private fun AdContentInfo.describe(): String =
        "src=$sourceName · eCPM=${formatRevenue(revenue)} · precision=$revenuePrecision · depth=$impressionDepth"

    private fun formatRevenue(value: Double): String = "%.4f".format(value)
}
