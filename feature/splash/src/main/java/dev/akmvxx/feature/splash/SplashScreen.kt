package dev.akmvxx.feature.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.akmvxx.feature.splash.ui.InfoSection
import dev.akmvxx.feature.splash.ui.Loader
import dev.akmvxx.feature.splash.ui.LogoWithTitleSection
import dev.akmvxx.ui.AppColors

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SplashContent(state = state)
}

@Composable
private fun SplashContent(state: SplashState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.BackgroundPrimary)
            .systemBarsPadding(),
    ) {
        InfoSection(
            isLoading = state.isLoading,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(24.dp)
        )
        LogoWithTitleSection(state.isLoading)
        Loader(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp),
            isLoading = state.isLoading
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashPreview() {
    SplashContent(state = SplashState(isLoading = false))
}
