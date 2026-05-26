package dev.akmvxx.ads.nativead

import android.view.ViewGroup
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
internal fun NativeAdView(slotKey: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val holder = remember(slotKey) {
        NativeAdSlots.acquireInline(context, slotKey)
    } ?: return

    AndroidView(
        modifier = modifier.border(1.dp, AppColors.Outlined, RoundedCornerShape(8.dp)),
        factory = {
            (holder.views.root.parent as? ViewGroup)?.removeView(holder.views.root)
            holder.views.root.doOnAttach { holder.views.root.bindAdContent(holder.ad) }
            holder.views.root
        }
    )
}
