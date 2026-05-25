package dev.akmvxx.feature.splash

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.akmvxx.android.MVI
import dev.akmvxx.domain.useCases.settings.GetSettingsUseCase
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
) : MVI<SplashIntent, SplashState, SplashEvent>(SplashState()) {

    override suspend fun handleIntent(intent: SplashIntent) = Unit
}
