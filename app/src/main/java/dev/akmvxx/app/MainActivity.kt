package dev.akmvxx.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.akmvxx.ads.NativeCoordinator
import dev.akmvxx.ads.interstitial.InterstitialCoordinator
import dev.akmvxx.ads.open.OpenCoordinator
import dev.akmvxx.android.SnackbarManager
import dev.akmvxx.common.Result
import dev.akmvxx.domain.entity.settings.SettingsEntity
import dev.akmvxx.domain.useCases.settings.GetSettingsUseCase
import dev.akmvxx.ui.utils.StatusBarColor
import dev.akmvxx.ui.utils.changeStatusBarColor
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var snackbarManager: SnackbarManager

    @Inject
    lateinit var getSettingsUseCase: GetSettingsUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdServices()
        enableEdgeToEdge()
        setContent {
            changeStatusBarColor(StatusBarColor.Light)
            RootContent(snackbarManager = snackbarManager)
        }
    }

    private fun initAdServices() = lifecycleScope.launch {
        val result = getSettingsUseCase.fetch()
        val settings = when (result) {
            is Result.Success -> result.data
            is Result.Error -> result.data ?: return@launch
        }
        initCoordinators(settings)
    }

    private fun initCoordinators(settings: SettingsEntity) {
        val casId = BuildConfig.APPLICATION_ID
        val appContext = applicationContext
        OpenCoordinator.initialize(appContext, casId, settings)
        NativeCoordinator.initialize(appContext, casId, settings)
        InterstitialCoordinator.initialize(appContext, casId, settings)
    }

    override fun onStart() {
        super.onStart()
        InterstitialCoordinator.start()
    }

    override fun onStop() {
        super.onStop()
        InterstitialCoordinator.stop()
    }

    override fun onPause() {
        super.onPause()
        OpenCoordinator.pause()
    }

    override fun onResume() {
        super.onResume()
        OpenCoordinator.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        OpenCoordinator.destroy()
        NativeCoordinator.destroy()
        InterstitialCoordinator.destroy()
    }
}
