package dev.akmvxx.app

import android.content.Context
import android.util.Log
import com.cleversolutions.ads.AdType
import com.cleversolutions.ads.android.CAS
import com.facebook.ads.AdSettings

private const val TAG = "Ads"

fun Context.setupCas(casId: String) {
    AdSettings.setDataProcessingOptions(arrayOf())
    CAS.settings.debugMode = BuildConfig.DEBUG

    CAS.buildManager()
        .withCasId(casId)
        .withAdTypes(
            AdType.Interstitial,
            AdType.AppOpen,
            AdType.Native,
            AdType.Banner,
        )
        .withCompletionListener { config ->
            Log.d(
                TAG,
                "CAS init · sdk=${CAS.getSDKVersion()}" +
                    " · err=${config.error}" +
                    " · country=${config.countryCode}" +
                    " · consentRequired=${config.isConsentRequired}",
            )
        }
        .build(this)
}
