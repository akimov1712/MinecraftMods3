package dev.akmvxx.feature.splash.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.akmvxx.ui.AppColors

@Composable
internal fun Logo(isLoading: Boolean) {
    val animateSizeLogo by animateDpAsState(if (isLoading) 180.dp else 140.dp)
    Image(
        painter = painterResource(dev.akmvxx.ui.R.drawable.logo),
        contentDescription = null,
        modifier = Modifier
            .size(animateSizeLogo)
            .clip(RoundedCornerShape(40.dp))
            .background(AppColors.BackgroundSecondary)
            .border(1.dp, AppColors.Outlined, RoundedCornerShape(40.dp))
            .padding(8.dp)
            .clip(RoundedCornerShape(32.dp))
    )
}