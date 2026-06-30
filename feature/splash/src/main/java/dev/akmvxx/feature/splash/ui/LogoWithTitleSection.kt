package dev.akmvxx.feature.splash.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import dev.akmvxx.ui.AppColors

@Composable
internal fun LogoWithTitleSection(isLoading: Boolean) {
    val animateAlphaBackgroundGradient by animateFloatAsState(if (isLoading) 0f else 1f)
    Box(
        modifier = Modifier
            .alpha(animateAlphaBackgroundGradient)
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(AppColors.Primary.copy(0.5f), AppColors.BackgroundPrimary),
                )
            )
    )
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Logo(isLoading)
            Spacer(Modifier.height(24.dp))
            Title(isLoading)
        }
        Box(
            modifier = Modifier
                .animateContentSize()
                .fillMaxHeight(if (isLoading) 0f else 0.5f)
        )
    }
}
