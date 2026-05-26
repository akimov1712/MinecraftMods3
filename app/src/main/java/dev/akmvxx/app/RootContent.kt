package dev.akmvxx.app

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import dev.akmvxx.ads.open.AppOpenAds
import dev.akmvxx.android.SnackbarManager
import dev.akmvxx.navigation.RootNavKey
import dev.akmvxx.navigation.rootNavigator
import dev.akmvxx.ui.components.AppSnackbarHost
import dev.akmvxx.ui.utils.ObserveAsEvents

@Composable
fun RootContent(
    snackbarManager: SnackbarManager
) {
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(snackbarManager.messages) {
        snackbarHostState.showSnackbar(it)
    }

    val activity = LocalActivity.current
    val navigator = rootNavigator()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, activity) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START && activity != null) {
                val current = navigator.current
                val onAdShow = current is RootNavKey.AdShow
                val onSplash = current is RootNavKey.Splash
                if (!onAdShow && !onSplash) AppOpenAds.show(activity)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        RootNavigation()
        AppSnackbarHost(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .systemBarsPadding()
                .imePadding()
                .padding(24.dp),
            snackbarHostState = snackbarHostState
        )
    }
}
