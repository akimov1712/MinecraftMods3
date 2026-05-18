package dev.akmvxx.feature.splash.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.akmvxx.ui.AppColors

@Composable
internal fun Title(isLoading: Boolean) {
    val animateFontSize by animateIntAsState(if (isLoading) 28 else 20)
    val animateWidth by animateDpAsState(if (isLoading) 220.dp else 180.dp)
    Text(
        modifier = Modifier.width(animateWidth),
        text = stringResource(dev.akmvxx.ui.R.string.app_name),
        textAlign = TextAlign.Center,
        color = AppColors.TextWhite,
        fontWeight = FontWeight.Bold,
        fontSize = animateFontSize.sp
    )
}