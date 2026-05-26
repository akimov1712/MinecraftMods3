package dev.akmvxx.ads

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.akmvxx.ads.interstitial.InterstitialAds
import dev.akmvxx.ads.nativead.NativeAdPool
import dev.akmvxx.ads.open.AppOpenAds
import dev.akmvxx.common.Result
import dev.akmvxx.domain.entity.settings.SettingsEntity
import dev.akmvxx.domain.useCases.settings.GetSettingsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Owns the lifetime of every ad coordinator: fetches the server-driven
 * settings once, initializes the coordinators with the resolved config,
 * and wires the process-wide foreground/background signal to interstitial
 * load gating.
 *
 * Should be invoked exactly once from [android.app.Application.onCreate].
 */
@Singleton
class AdsBootstrap @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getSettings: GetSettingsUseCase,
) {

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
    private var started = false

    fun start(casId: String) {
        if (started) return
        started = true

        scope.launch {
            val settings = resolveSettings() ?: return@launch
            initializeAds(casId, settings)
        }

        observeProcessLifecycle()
    }

    private suspend fun resolveSettings(): SettingsEntity? =
        when (val result = getSettings.fetch()) {
            is Result.Success -> result.data
            is Result.Error -> result.data
        }

    private fun initializeAds(casId: String, settings: SettingsEntity) {
        AppOpenAds.initialize(context, casId, settings)
        InterstitialAds.initialize(context, casId, settings)
        NativeAds.initialize(context, casId, settings)
    }

    private fun observeProcessLifecycle() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> InterstitialAds.start()
                    Lifecycle.Event.ON_STOP -> InterstitialAds.stop()
                    else -> Unit
                }
            }
        )
    }

    /**
     * Mirror of the lifecycle pause/resume the activity used to broadcast;
     * exposed for the foreground composable observer that calls
     * [AppOpenAds.show] on each app open.
     */
    fun pauseAppOpen() = AppOpenAds.pause()
    fun resumeAppOpen() = AppOpenAds.resume()
}
