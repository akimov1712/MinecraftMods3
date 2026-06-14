package dev.akmvxx.app

import android.app.Activity
import android.util.Log
import com.yodo1.mas.Yodo1Mas
import com.yodo1.mas.Yodo1MasSdkConfiguration
import com.yodo1.mas.appopenad.Yodo1MasAppOpenAd
import com.yodo1.mas.error.Yodo1MasError
import com.yodo1.mas.helper.model.Yodo1MasAdBuildConfig
import com.yodo1.mas.interstitial.Yodo1MasInterstitialAd

private const val TAG = "Ads"

/**
 * Initializes the Yodo1 MAS SDK.
 *
 * Order matters — per the docs every pre-init flag (auto-delay throttles, the
 * built-in privacy dialog, GDPR / CCPA / COPPA toggles) must be applied before
 * [Yodo1Mas.initMas]. After this returns the MAS managers are ready to
 * receive load() calls from the coordinator facades.
 *
 * The built-in privacy dialog handles GDPR (EU), CCPA (California) and COPPA
 * in one unified flow that MAS shows automatically on first launch for users
 * in regulated regions. That is enough for worldwide compliance — no
 * additional UMP integration is required for the basic Google Ads waterfall.
 *
 * @param activity  Activity context required by [Yodo1Mas.initMas].
 * @param appKey    MAS App Key from the Yodo1 dashboard.
 * @param onReady   Invoked on the main thread when SDK init completes.
 * @param onFailed  Invoked on the main thread if init fails — MAS keeps
 *                  retrying internally, but we surface it for logging.
 */
fun setupMas(
    activity: Activity,
    appKey: String,
    onReady: () -> Unit,
    onFailed: (Yodo1MasError) -> Unit,
) {
    // ── Pre-init configuration ───────────────────────────────────────────
    // Only the per-format singletons that exist in the MAS API surface
    // expose autoDelayIfLoadFail — Banner and Native views configure their
    // own auto-load on the view itself, not on a static singleton.
    Yodo1MasInterstitialAd.getInstance().autoDelayIfLoadFail = true
    Yodo1MasAppOpenAd.getInstance().autoDelayIfLoadFail = true

    // Built-in MAS privacy dialog. The SDK detects the user's region and
    // shows the right form (GDPR consent in EU, CCPA opt-out in California,
    // COPPA age gate where applicable). Disabling this would force us to
    // ship our own consent flow + UMP integration — much more error-prone.
    val config = Yodo1MasAdBuildConfig.Builder()
        .enableUserPrivacyDialog(true)
        .build()
    Yodo1Mas.getInstance().setAdBuildConfig(config)

    Yodo1Mas.getInstance().initMas(
        activity,
        appKey,
        object : Yodo1Mas.InitListener {
            override fun onMasInitSuccessful() {
                Log.d(TAG, "MAS init success")
                onReady()
            }

            override fun onMasInitSuccessful(info: Yodo1MasSdkConfiguration?) {
                Log.d(TAG, "MAS init success · info=$info")
                onReady()
            }

            override fun onMasInitFailed(error: Yodo1MasError) {
                Log.d(TAG, "MAS init failed · code=${error.code} · ${error.message}")
                onFailed(error)
            }
        },
    )
}
