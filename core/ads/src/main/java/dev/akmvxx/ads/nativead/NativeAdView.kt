package dev.akmvxx.ads.nativead

import android.view.ViewGroup
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.yodo1.mas.error.Yodo1MasError
import com.yodo1.mas.nativeads.Yodo1MasNativeAdListener
import com.yodo1.mas.nativeads.Yodo1MasNativeAdRevenueListener
import com.yodo1.mas.nativeads.Yodo1MasNativeAdView
import dev.akmvxx.ads.AdEvents
import dev.akmvxx.ui.AppColors

private const val TYPE = "Native"

@Composable
internal fun NativeAdView(slotKey: String, modifier: Modifier = Modifier) {
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
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .border(1.dp, AppColors.Outlined, RoundedCornerShape(8.dp)),
        factory = {
            (nativeAdView.parent as? ViewGroup)?.removeView(nativeAdView)
            nativeAdView
        },
    )

    DisposableEffect(slotKey) {
        onDispose { nativeAdView.destroy() }
    }
}
