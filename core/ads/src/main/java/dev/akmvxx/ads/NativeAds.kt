package dev.akmvxx.ads

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.akmvxx.ads.banner.BannerAdPool
import dev.akmvxx.ads.banner.BannerAdView
import dev.akmvxx.ads.nativead.FullscreenNativeAdView
import dev.akmvxx.ads.nativead.NativeAdPool
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
    private var showChance: Int = 100
    private var active by mutableStateOf<ActiveType>(ActiveType.None)

    fun initialize(context: Context, casId: String, settings: SettingsEntity) {
        if (initialized) return
        if (!settings.adEnabled.native) return

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
    }

    fun setOnPreload(onPreloaded: (PreloadStatus) -> Unit) {
        if (!initialized) {
            onPreloaded(PreloadStatus.NONE)
            return
        }
        when (active) {
            ActiveType.Banner -> BannerAdPool.setListener(onPreloaded)
            ActiveType.Native -> NativeAdPool.setCallback(onPreloaded)
            ActiveType.None -> onPreloaded(PreloadStatus.NONE)
        }
    }

    fun clearOnPreload() {
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

    @Composable
    fun Show(slot: Slot, modifier: Modifier = Modifier) {
        if (!initialized) return
        if (!isShowNextAd(showChance)) return

        when (active) {
            ActiveType.Banner -> BannerAdView(modifier)
            ActiveType.Native -> when (slot) {
                Slot.Fullscreen -> FullscreenNativeAdView()
                Slot.Inline -> NativeAdView(modifier = modifier)
            }

            ActiveType.None -> Unit
        }
    }

    fun destroy() {
        if (!initialized) return
        when (active) {
            ActiveType.Banner -> BannerAdPool.destroy()
            ActiveType.Native -> NativeAdPool.destroy()
            ActiveType.None -> Unit
        }
        active = ActiveType.None
        initialized = false
    }
}
