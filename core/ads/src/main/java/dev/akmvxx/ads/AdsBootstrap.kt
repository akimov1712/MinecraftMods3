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
import dev.akmvxx.domain.entity.settings.AdEnabledEntity
import dev.akmvxx.domain.entity.settings.SettingsEntity
import dev.akmvxx.domain.useCases.settings.GetSettingsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Orchestrates the entire ad subsystem startup.
 *
 * Order of operations:
 *   1. fetch settings from the server (repository handles retries + cache);
 *   2. spin up the CAS SDK on a background dispatcher so the blocking
 *      `build()` call never lands on the main thread (avoids ANRs);
 *   3. initialize the public ad facades (Interstitial / AppOpen / Native)
 *      back on the main thread because their internal state machines and
 *      Compose listeners expect that.
 *
 * The lambda for `setupCas` lives in the `:app` module so `:core:ads`
 * doesn't have to depend on Facebook's AdSettings or anything app-specific.
 */
@Singleton
class AdsBootstrap @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getSettings: GetSettingsUseCase,
) {

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
    private var started = false

    fun start(
        casId: String,
        setupCas: (casId: String, enabled: AdEnabledEntity) -> Unit,
    ) {
        if (started) return
        started = true

        scope.launch {
            val settings = resolveSettings() ?: return@launch

            // Heavy SDK init: stays off the main thread.
            withContext(Dispatchers.IO) {
                setupCas(casId, settings.adEnabled)
            }

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
}
