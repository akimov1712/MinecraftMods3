package dev.akmvxx.feature.splash.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.akmvxx.ads.NativeAds

private const val SPLASH_NATIVE_SLOT = "splash_native"

@Composable
internal fun SplashNativeSection(modifier: Modifier = Modifier) {
    if (!NativeAds.hasAd()) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        NativeAds.Show(
            slot = NativeAds.Slot.Inline,
            slotKey = SPLASH_NATIVE_SLOT,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
