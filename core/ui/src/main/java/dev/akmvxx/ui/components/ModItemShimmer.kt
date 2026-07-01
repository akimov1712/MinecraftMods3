package dev.akmvxx.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import dev.akmvxx.ui.AppColors

private val CardShape = RoundedCornerShape(24.dp)
private val ImageShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)

@Composable
fun ModItemShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardShape)
            .background(AppColors.BackgroundSecondary)
            .border(width = 1.dp, color = AppColors.White.copy(alpha = 0.06f), shape = CardShape)
            .shimmer()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 10f)
                .clip(ImageShape)
                .background(AppColors.Shimmer)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ShimmerBlock(width = 200.dp, height = 20.dp, shape = RoundedCornerShape(6.dp))
            ShimmerBlock(width = 130.dp, height = 18.dp, shape = RoundedCornerShape(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                ShimmerBlock(width = 70.dp, height = 14.dp)
                Spacer(Modifier.weight(1f))
                ShimmerBlock(width = 120.dp, height = 40.dp)
            }
        }
    }
}

@Composable
private fun ShimmerBlock(
    width: Dp,
    height: Dp,
    shape: Shape = CircleShape,
) {
    Box(
        modifier = Modifier
            .size(width = width, height = height)
            .clip(shape)
            .background(AppColors.Shimmer.copy(alpha = 0.35f))
    )
}
