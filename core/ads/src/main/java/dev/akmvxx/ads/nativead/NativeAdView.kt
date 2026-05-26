package dev.akmvxx.ads.nativead

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.doOnAttach
import dev.akmvxx.ui.AppColors

@Composable
internal fun NativeAdView(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val nativeAd = remember { NativeAdPool.pop() } ?: return
    val views = remember { buildInlineNativeAdView(context) }

    AndroidView(
        modifier = modifier.border(1.dp, AppColors.Outlined, RoundedCornerShape(8.dp)),
        factory = {
            views.bindToRoot()
            views.root.doOnAttach { views.root.bindAdContent(nativeAd) }
            views.root
        }
    )
}
