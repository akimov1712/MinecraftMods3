package dev.akmvxx.ads

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.akmvxx.ads.banner.BannerAdPool
import dev.akmvxx.ads.banner.BannerAdSlots
import dev.akmvxx.ads.banner.BannerAdView
import dev.akmvxx.ads.nativead.FullscreenNativeAdView
import dev.akmvxx.ads.nativead.NativeAdPool
import dev.akmvxx.ads.nativead.NativeAdSlots
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

    fun initialize(context: Context, casId: String, settings: SettingsEntity) {
        if (initialized || disabled) return

        if (!settings.adEnabled.native) {
            disabled = true
            consumePending()?.invoke(PreloadStatus.NONE)
            return
        }

        initialized = true
        showChance = settings.adChangePercent.native

        active = when (settings.nativeType) {
            BANNER -> {
                BannerAdPool.initialize(context.applicationContext, casId, settings.preloadSize)
                BannerAdPool.startPreload()
                ActiveType.Banner
            }

            NATIVE -> {
                NativeAdPool.init(context.applicationContext, casId, settings.preloadSize)
                NativeAdPool.load()
                ActiveType.Native
            }
        }

        consumePending()?.let { wirePreloadCallback(it) }
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
        wirePreloadCallback(onPreloaded)
    }

    fun clearOnPreload() {
        pendingPreloadCallback = null
        if (!initialized) return
        when (active) {
            ActiveType.Banner -> BannerAdPool.clearListener()
            ActiveType.Native -> NativeAdPool.clearCallback()
            ActiveType.None -> Unit
        }
    }

    fun hasAd(): Boolean {
        if (!initialized) return false
        return when (active) {
            ActiveType.Native -> NativeAdPool.hasAd()
            ActiveType.Banner -> BannerAdPool.hasAd()
            ActiveType.None -> false
        }
    }

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
        if (initialized) {
            when (active) {
                ActiveType.Banner -> {
                    BannerAdSlots.releaseAll()
                    BannerAdPool.destroy()
                }
                ActiveType.Native -> {
                    NativeAdSlots.releaseAll()
                    NativeAdPool.destroy()
                }
                ActiveType.None -> Unit
            }
        }
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

    private fun wirePreloadCallback(callback: (PreloadStatus) -> Unit) {
        when (active) {
            ActiveType.Banner -> BannerAdPool.setListener(callback)
            ActiveType.Native -> NativeAdPool.setCallback(callback)
            ActiveType.None -> callback(PreloadStatus.NONE)
        }
    }
}
