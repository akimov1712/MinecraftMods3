package dev.akmvxx.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import dev.akmvxx.ads.AdsBootstrap
import dev.akmvxx.android.SnackbarManager
import dev.akmvxx.ui.utils.StatusBarColor
import dev.akmvxx.ui.utils.changeStatusBarColor
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var snackbarManager: SnackbarManager

    @Inject
    lateinit var appReview: AppReview

    @Inject
    lateinit var adsBootstrap: AdsBootstrap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appReview.maybeRequest(this)
        adsBootstrap.start(
            activity = this,
            appKey = BuildConfig.MAS_APP_KEY,
        ) { activity, key, onReady, onFailed ->
            setupMas(activity, key, onReady) { onFailed() }
        }
        enableEdgeToEdge()
        setContent {
            changeStatusBarColor(StatusBarColor.Light)
            RootContent(snackbarManager = snackbarManager)
        }
    }
}
