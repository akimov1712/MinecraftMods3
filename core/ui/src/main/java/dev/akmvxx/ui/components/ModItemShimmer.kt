package dev.akmvxx.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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

@Composable
fun ModItemShimmer() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.1f)
            .clip(RoundedCornerShape(28.dp))
            .background(AppColors.BackgroundSecondary)
            .shimmer()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.Shimmer)
        )

        ShimmerFileCountChip()

        ShimmerInfoBlock()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = AppColors.White.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(28.dp)
                )
        )
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
            .background(AppColors.BackgroundSecondary)
    )
}

@Composable
private fun BoxScope.ShimmerFileCountChip() {
    Row(
        modifier = Modifier
            .align(Alignment.TopStart)
            .padding(12.dp)
            .clip(CircleShape)
            .background(AppColors.Black.copy(alpha = 0.35f))
            .padding(start = 4.dp, top = 4.dp, bottom = 4.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(AppColors.BackgroundSecondary)
        )
        ShimmerBlock(width = 52.dp, height = 12.dp)
    }
}

@Composable
private fun BoxScope.ShimmerInfoBlock() {
    Row(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ShimmerBlock(width = 60.dp, height = 18.dp)
            ShimmerBlock(
                width = 180.dp,
                height = 20.dp,
                shape = RoundedCornerShape(6.dp)
            )
            Spacer(Modifier.height(2.dp))
            ShimmerBlock(
                width = 120.dp,
                height = 16.dp,
                shape = RoundedCornerShape(6.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(AppColors.BackgroundSecondary)
        )
    }
}
