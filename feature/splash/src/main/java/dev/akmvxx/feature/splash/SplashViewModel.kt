package dev.akmvxx.feature.splash

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.akmvxx.android.MVI
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() :
    MVI<SplashIntent, SplashState, SplashEvent>(SplashState()) {

    init {

        markReady()
        startWatchdog()
    }

    private fun startWatchdog() {
        viewModelScope.launch {
            delay(WATCHDOG_DELAY_MS)
            markReady()
        }
    }

    private fun markReady() {
        if (!_state.value.isLoading) return
        _state.update { it.copy(isLoading = false) }
    }

    override suspend fun handleIntent(intent: SplashIntent) = Unit

    private companion object {
        const val WATCHDOG_DELAY_MS = 6_000L
    }
}
