package dev.akmvxx.feature.splash

data class SplashState(
    val isLoading: Boolean = true
)

sealed interface SplashIntent

sealed interface SplashEvent