package dev.akmvxx.ads

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.akmvxx.ads.banner.BannerCasPreloader
import dev.akmvxx.ads.banner.BannerCasView
import dev.akmvxx.ads.nativead.FullscreenNativeCasView
import dev.akmvxx.ads.nativead.NativeCasController
import dev.akmvxx.ads.nativead.NativeCasView
import dev.akmvxx.ads.util.isShowNextAd
import dev.akmvxx.domain.entity.settings.SettingsEntity
import dev.akmvxx.domain.entity.settings.SettingsEntity.NativeType.BANNER
import dev.akmvxx.domain.entity.settings.SettingsEntity.NativeType.NATIVE

object NativeCoordinator {

    private sealed interface AdType {
        data object None : AdType
        data object Native : AdType
        data object Banner : AdType
    }

    enum class ViewAdType {
        Fullscreen, Native
    }

    private var initialized = false
    private var percentShow: Int = 100
    private var activeAdType by mutableStateOf<AdType>(AdType.None)

    fun initialize(context: Context, casId: String, settings: SettingsEntity) {
        if (initialized) return
        if (!settings.adEnabled.native) return

        initialized = true
        percentShow = settings.adChangePercent.native

        activeAdType = when (settings.nativeType) {
            BANNER -> {
                BannerCasPreloader.initialize(context.applicationContext, casId, settings.preloadSize)
                BannerCasPreloader.startPreload()
                AdType.Banner
            }

            NATIVE -> {
                NativeCasController.init(context.applicationContext, casId, settings.preloadSize)
                NativeCasController.load()
                AdType.Native
            }
        }
    }

    fun setOnPreload(onPreloaded: (PreloadStatus) -> Unit) {
        if (!initialized) {
            onPreloaded(PreloadStatus.NONE)
            return
        }
        when (activeAdType) {
            AdType.Banner -> BannerCasPreloader.setListener(onPreloaded)
            AdType.Native -> NativeCasController.setCallback(onPreloaded)
            AdType.None -> onPreloaded(PreloadStatus.NONE)
        }
    }

    fun clearOnPreload() {
        if (!initialized) return
        when (activeAdType) {
            AdType.Banner -> BannerCasPreloader.deleteListener()
            AdType.Native -> NativeCasController.deleteCallback()
            AdType.None -> Unit
        }
    }

    fun hasAd(): Boolean {
        if (!initialized) return false
        return when (activeAdType) {
            AdType.Native -> NativeCasController.hasAd()
            AdType.Banner -> BannerCasPreloader.hasAd()
            AdType.None -> false
        }
    }

    fun isSelectedBannerAdType(): Boolean = activeAdType is AdType.Banner

    @Composable
    fun Show(type: ViewAdType, modifier: Modifier = Modifier) {
        if (!initialized) return
        if (!isShowNextAd(percentShow)) return

        when (activeAdType) {
            AdType.Banner -> BannerCasView(modifier)
            AdType.Native -> when (type) {
                ViewAdType.Fullscreen -> FullscreenNativeCasView()
                ViewAdType.Native -> NativeCasView(modifier = modifier)
            }
            AdType.None -> Unit
        }
    }

    fun destroy() {
        if (!initialized) return
        when (activeAdType) {
            AdType.Banner -> BannerCasPreloader.destroy()
            AdType.Native -> NativeCasController.destroy()
            AdType.None -> Unit
        }
        activeAdType = AdType.None
        initialized = false
    }
}
