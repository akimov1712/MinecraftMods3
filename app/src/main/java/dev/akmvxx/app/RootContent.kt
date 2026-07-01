package dev.akmvxx.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.akmvxx.android.SnackbarManager
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
