package dev.akmvxx.ads.nativead

import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.doOnLayout

@Composable
internal fun FullscreenNativeAdView(slotKey: String) {
    val context = LocalContext.current
    val holder = remember(slotKey) {
        NativeAdSlots.acquireFullscreen(context, slotKey)
    } ?: return

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            (holder.views.root.parent as? ViewGroup)?.removeView(holder.views.root)
            holder.views.root.doOnLayout {
                if (!holder.bound) {
                    holder.bound = true
                    holder.views.root.bindAdContent(holder.ad)
                }
            }
            holder.views.root
        }
    )
}
