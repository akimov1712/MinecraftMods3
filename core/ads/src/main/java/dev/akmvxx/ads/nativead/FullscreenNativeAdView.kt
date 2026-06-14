package dev.akmvxx.ads.nativead

import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.yodo1.mas.error.Yodo1MasError
import com.yodo1.mas.nativeads.Yodo1MasNativeAdListener
import com.yodo1.mas.nativeads.Yodo1MasNativeAdRevenueListener
import com.yodo1.mas.nativeads.Yodo1MasNativeAdView
import dev.akmvxx.ads.AdEvents

private const val TYPE = "NativeFullscreen"

@Composable
internal fun FullscreenNativeAdView(slotKey: String) {
    val context = LocalContext.current
    val nativeAdView = remember(slotKey) {
        Yodo1MasNativeAdView(context).apply {
            setAdListener(object : Yodo1MasNativeAdListener {
                override fun onNativeAdLoaded(view: Yodo1MasNativeAdView) =
                    AdEvents.loaded(TYPE)

                override fun onNativeAdFailedToLoad(
                    view: Yodo1MasNativeAdView,
                    error: Yodo1MasError,
                ) = AdEvents.failed("$TYPE load", error)
            })
            setAdRevenueListener(
                Yodo1MasNativeAdRevenueListener { _, value ->
                    AdEvents.impression(TYPE, value)
                }
            )
            loadAd()
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            (nativeAdView.parent as? ViewGroup)?.removeView(nativeAdView)
            nativeAdView
        },
    )

    DisposableEffect(slotKey) {
        onDispose { nativeAdView.destroy() }
    }
}
