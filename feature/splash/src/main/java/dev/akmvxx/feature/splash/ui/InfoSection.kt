package dev.akmvxx.feature.splash.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

@Composable
internal fun InfoSection(
    modifier: Modifier = Modifier,
    isLoading: Boolean
) {
    val animateAlpha by animateFloatAsState(if (isLoading) 0f else 1f)
    Column(
        modifier = modifier.alpha(animateAlpha)
    ) {
        StartSection()
    }
}