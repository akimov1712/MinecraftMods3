package dev.akmvxx.feature.browse.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R

@Composable
internal fun BrowseHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.screen_browse),
            color = AppColors.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.5).sp,
            modifier = Modifier.drawBehind {
                val markerHeight = size.height * 0.42f
                drawRoundRect(
                    color = AppColors.Primary.copy(alpha = 0.28f),
                    topLeft = Offset(x = -6f, y = size.height - markerHeight),
                    size = Size(width = size.width + 12f, height = markerHeight),
                    cornerRadius = CornerRadius(6f, 6f),
                )
            },
        )
    }
}
