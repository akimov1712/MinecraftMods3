package dev.akmvxx.ads

import android.util.Log
import com.cleveradssolutions.sdk.AdContentInfo
import com.cleversolutions.ads.AdError

internal object AdEvents {

    private const val TAG = "CAS_AD_APP"

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
