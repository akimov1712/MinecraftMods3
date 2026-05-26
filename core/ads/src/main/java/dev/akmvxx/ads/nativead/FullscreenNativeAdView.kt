package dev.akmvxx.ads.nativead

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.doOnLayout

@Composable
internal fun FullscreenNativeAdView() {
    val context = LocalContext.current
    val ad = remember { NativeAdPool.pop() } ?: return
    val views = remember { buildFullscreenNativeAdView(context) }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            views.bindToRoot()
            views.root.doOnLayout { views.root.bindAdContent(ad) }
            views.root
        }
    )
}
