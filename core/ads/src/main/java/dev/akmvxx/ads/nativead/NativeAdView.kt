package dev.akmvxx.ads.nativead

import android.view.ViewGroup
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.doOnAttach
import dev.akmvxx.ui.AppColors
import kotlinx.coroutines.delay

private const val ACQUIRE_MAX_ATTEMPTS = 10
private const val ACQUIRE_RETRY_MS = 500L

@Composable
internal fun NativeAdView(slotKey: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var holder by remember(slotKey) {
        mutableStateOf<NativeAdSlots.Holder?>(NativeAdSlots.acquireInline(context, slotKey))
    }

    // Pool may still be filling when the slot first composes — retry a few
    // times instead of leaving the slot empty for the whole session.
    LaunchedEffect(slotKey) {
        var attempt = 0
        while (holder == null && attempt < ACQUIRE_MAX_ATTEMPTS) {
            delay(ACQUIRE_RETRY_MS)
            holder = NativeAdSlots.acquireInline(context, slotKey)
            attempt++
        }
    }

    val resolved = holder ?: return

    AndroidView(
        modifier = modifier.border(1.dp, AppColors.Outlined, RoundedCornerShape(8.dp)),
        factory = {
            (resolved.views.root.parent as? ViewGroup)?.removeView(resolved.views.root)
            resolved.views.root.doOnAttach {
                if (!resolved.bound) {
                    resolved.bound = true
                    resolved.views.root.bindAdContent(resolved.ad)
                }
            }
            resolved.views.root
        }
    )
}
