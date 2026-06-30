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

fun setupMas(
    activity: Activity,
    appKey: String,
    onReady: () -> Unit,
    onFailed: (Yodo1MasError) -> Unit,
) {

    Yodo1MasInterstitialAd.getInstance().autoDelayIfLoadFail = true
    Yodo1MasAppOpenAd.getInstance().autoDelayIfLoadFail = true

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
