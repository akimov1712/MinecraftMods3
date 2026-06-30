package dev.akmvxx.ads

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.akmvxx.ads.banner.BannerAdView
import dev.akmvxx.ads.nativead.FullscreenNativeAdView
import dev.akmvxx.ads.nativead.NativeAdView
import dev.akmvxx.ads.util.isShowNextAd
import dev.akmvxx.domain.entity.settings.SettingsEntity
import dev.akmvxx.domain.entity.settings.SettingsEntity.NativeType.BANNER
import dev.akmvxx.domain.entity.settings.SettingsEntity.NativeType.NATIVE

object NativeAds {

    private sealed interface ActiveType {
        data object None : ActiveType
        data object Native : ActiveType
        data object Banner : ActiveType
    }

    enum class Slot { Inline, Fullscreen }

    private var initialized = false
    private var disabled = false
    private var pendingPreloadCallback: ((PreloadStatus) -> Unit)? = null
    private var showChance: Int = 100
    private var active by mutableStateOf<ActiveType>(ActiveType.None)

    fun initialize(context: Context, settings: SettingsEntity) {
        if (initialized || disabled) return

        if (!settings.adEnabled.native) {
            disabled = true
            consumePending()?.invoke(PreloadStatus.NONE)
            return
        }

        initialized = true
        showChance = settings.adChangePercent.native

        active = when (settings.nativeType) {
            BANNER -> ActiveType.Banner
            NATIVE -> ActiveType.Native
        }

        consumePending()?.invoke(PreloadStatus.PRELOADED)
    }

    fun setOnPreload(onPreloaded: (PreloadStatus) -> Unit) {
        if (disabled) {
            onPreloaded(PreloadStatus.NONE)
            return
        }
        if (!initialized) {

            pendingPreloadCallback = onPreloaded
            return
        }
        onPreloaded(PreloadStatus.PRELOADED)
    }

    fun clearOnPreload() {
        pendingPreloadCallback = null
    }

    fun hasAd(): Boolean = initialized

    fun isBanner(): Boolean = active is ActiveType.Banner

    fun isDisabled(): Boolean = disabled

    @Composable
    fun Show(slot: Slot, slotKey: String, modifier: Modifier = Modifier) {
        if (!initialized) return
        if (!isShowNextAd(showChance)) return

        when (active) {
            ActiveType.Banner -> BannerAdView(slotKey = slotKey, modifier = modifier)
            ActiveType.Native -> when (slot) {
                Slot.Fullscreen -> FullscreenNativeAdView(slotKey = slotKey)
                Slot.Inline -> NativeAdView(slotKey = slotKey, modifier = modifier)
            }
            ActiveType.None -> Unit
        }
    }

    fun destroy() {
        active = ActiveType.None
        initialized = false
        disabled = false
        pendingPreloadCallback = null
    }

    private fun consumePending(): ((PreloadStatus) -> Unit)? {
        val callback = pendingPreloadCallback
        pendingPreloadCallback = null
        return callback
    }
}
