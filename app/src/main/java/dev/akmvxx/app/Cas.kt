package dev.akmvxx.app

import android.content.Context
import android.util.Log
import com.cleversolutions.ads.AdType
import com.cleversolutions.ads.android.CAS
import dev.akmvxx.domain.entity.settings.AdEnabledEntity

private const val TAG = "Ads"

/**
 * Initializes the CAS mediation SDK. Should be called from a background
 * thread because the build() call blocks while spinning up every enabled
 * mediation adapter — that can take a couple of seconds on a cold start
 * and would otherwise trigger ANRs.
 *
 * Pass [enabled] from the resolved server settings so CAS only initializes
 * the adapters for ad formats we'll actually request — formats that the
 * server marks as disabled stay dormant and don't churn the network on
 * pointless preload calls.
 */
fun Context.setupCas(casId: String, enabled: AdEnabledEntity) {
    CAS.settings.debugMode = BuildConfig.DEBUG

    CAS.buildManager()
        .withCasId(casId)
        // Test ads on debug builds, real fill on release. Without this any
        // accidental clicks from QA devices land on a real placement and
        // AdMob can flag the account for invalid traffic.
        .withTestAdMode(BuildConfig.DEBUG)
        .withAdTypes(*enabled.toAdTypes())
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

private fun AdEnabledEntity.toAdTypes(): Array<AdType> = buildList {
    if (inter) add(AdType.Interstitial)
    if (open) add(AdType.AppOpen)
    if (native) {
        add(AdType.Native)
        add(AdType.Banner)
    }
}.toTypedArray()
