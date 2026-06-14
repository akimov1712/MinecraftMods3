package dev.akmvxx.ads

import android.util.Log
import com.yodo1.mas.error.Yodo1MasError
import com.yodo1.mas.ad.Yodo1MasAdValue

/**
 * Single logging channel for ad telemetry. Each impression line prints the
 * mediation network ([Yodo1MasAdValue] doesn't expose `sourceName` directly,
 * but we get currency + eCPM + revenue precision), so logcat is enough to
 * eyeball which networks are filling and what they pay.
 *
 *     adb logcat -s Ads
 */
internal object AdEvents {

    private const val TAG = "Ads"

    fun loaded(type: String) {
        Log.d(TAG, "$type loaded")
    }

    fun impression(type: String, value: Yodo1MasAdValue) {
        Log.d(
            TAG,
            "$type IMPRESSION · eCPM=${formatRevenue(value.getRevenue())}" +
                " · currency=${value.getCurrency()}" +
                " · precision=${value.getRevenuePrecision()}" +
                " · network=${value.getNetworkName()}",
        )
    }

    fun dismissed(type: String) {
        Log.d(TAG, "$type dismissed")
    }

    fun failed(stage: String, error: Yodo1MasError) {
        Log.d(TAG, "$stage FAILED · code=${error.code} · ${error.message}")
    }

    private fun formatRevenue(value: Double): String = "%.4f".format(value)
}
