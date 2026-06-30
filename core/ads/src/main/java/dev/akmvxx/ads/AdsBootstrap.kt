package dev.akmvxx.ads

import android.app.Activity
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.akmvxx.ads.interstitial.InterstitialAds
import dev.akmvxx.ads.open.AppOpenAds
import dev.akmvxx.common.Result
import dev.akmvxx.domain.entity.settings.SettingsEntity
import dev.akmvxx.domain.useCases.settings.GetSettingsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdsBootstrap @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getSettings: GetSettingsUseCase,
) {

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
    private var started = false

    fun start(
        activity: Activity,
        appKey: String,
        setupMas: (
            activity: Activity,
            appKey: String,
            onReady: () -> Unit,
            onFailed: () -> Unit,
        ) -> Unit,
    ) {
        if (started) return
        started = true

        if (appKey.isBlank()) {

            return
        }

        scope.launch {
            val settings = withContext(Dispatchers.IO) { resolveSettings() } ?: return@launch

            setupMas(
                activity,
                appKey,
                 { initializeAds(activity, settings) },
                 {  },
            )
        }
    }

    private suspend fun resolveSettings(): SettingsEntity? =
        when (val result = getSettings.fetch()) {
            is Result.Success -> result.data
            is Result.Error -> result.data
        }

    private fun initializeAds(activity: Activity, settings: SettingsEntity) {
        AppOpenAds.initialize(activity, settings)
        InterstitialAds.initialize(activity, settings)
        NativeAds.initialize(context, settings)
    }
}
