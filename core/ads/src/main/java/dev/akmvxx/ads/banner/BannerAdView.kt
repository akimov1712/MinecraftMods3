package dev.akmvxx.ads.banner

import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.yodo1.mas.banner.Yodo1MasBannerAdSize
import com.yodo1.mas.banner.Yodo1MasBannerAdView
import com.yodo1.mas.banner.Yodo1MasBannerAdListener
import com.yodo1.mas.banner.Yodo1MasBannerAdRevenueListener
import com.yodo1.mas.error.Yodo1MasError
import dev.akmvxx.ads.AdEvents
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R as UiR

private const val TYPE = "Banner"

/**
 * Composable wrapper around a single [Yodo1MasBannerAdView] instance. The view
 * is created once per [slotKey] (cached via `remember`) so that scrolling in a
 * LazyColumn doesn't churn through new banner instances on every recompose.
 *
 * The view's own auto-refresh loop keeps it fresh while it stays on screen, so
 * we don't manage refresh intervals in code.
 */
@Composable
internal fun BannerAdView(slotKey: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val bannerView = remember(slotKey) {
        Yodo1MasBannerAdView(context).apply {
            setAdSize(Yodo1MasBannerAdSize.Banner)
            setAdListener(object : Yodo1MasBannerAdListener {
                override fun onBannerAdLoaded(view: Yodo1MasBannerAdView) =
                    AdEvents.loaded(TYPE)

                override fun onBannerAdFailedToLoad(
                    view: Yodo1MasBannerAdView,
                    error: Yodo1MasError,
                ) = AdEvents.failed("$TYPE load", error)

                override fun onBannerAdOpened(view: Yodo1MasBannerAdView) = Unit

                override fun onBannerAdFailedToOpen(
                    view: Yodo1MasBannerAdView,
                    error: Yodo1MasError,
                ) = AdEvents.failed("$TYPE show", error)

                override fun onBannerAdClosed(view: Yodo1MasBannerAdView) =
                    AdEvents.dismissed(TYPE)
            })
            setAdRevenueListener(
                Yodo1MasBannerAdRevenueListener { _, value ->
                    AdEvents.impression(TYPE, value)
                }
            )
            loadAd()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(24.dp))
            .border(2.dp, AppColors.Primary, RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center,
    ) {
        AndroidView(
            factory = {
                (bannerView.parent as? ViewGroup)?.removeView(bannerView)
                bannerView
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
        )

        Text(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.TopStart)
                .background(Color.Black.copy(0.4f), RoundedCornerShape(16.dp))
                .padding(horizontal = 8.dp, vertical = 2.dp),
            text = stringResource(UiR.string.ad),
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
        )
    }

    DisposableEffect(slotKey) {
        onDispose {
            // Release SDK resources held by the view (auto-refresh timer,
            // bidder callbacks). MAS will free the underlying ad slot.
            bannerView.destroy()
        }
    }
}
