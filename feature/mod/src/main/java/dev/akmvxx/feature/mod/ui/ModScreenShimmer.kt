package dev.akmvxx.feature.mod.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import dev.akmvxx.ui.AppColors

private val HorizontalPadding = 20.dp

@Composable
internal fun ModScreenShimmer(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .shimmer(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.9f)
                .background(AppColors.Shimmer),
        )

        Spacer(Modifier.height(20.dp))

        ShimmerBlock(
            width = 80.dp,
            height = 26.dp,
            shape = CircleShape,
            modifier = Modifier.padding(horizontal = HorizontalPadding),
        )

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier.padding(horizontal = HorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            ShimmerBlock(
                width = 260.dp,
                height = 26.dp,
                shape = RoundedCornerShape(8.dp),
            )
            ShimmerBlock(
                width = 180.dp,
                height = 26.dp,
                shape = RoundedCornerShape(8.dp),
            )
        }

        Spacer(Modifier.height(28.dp))

        DescriptionShimmer(
            modifier = Modifier.padding(horizontal = HorizontalPadding),
        )

        Spacer(Modifier.height(32.dp))

        VersionsSectionShimmer(
            modifier = Modifier.padding(horizontal = HorizontalPadding),
        )

        Spacer(Modifier.height(32.dp))

        FilesSectionShimmer(
            modifier = Modifier.padding(horizontal = HorizontalPadding),
        )

        Spacer(Modifier.height(140.dp))
    }
}

@Composable
private fun DescriptionShimmer(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        SectionTitleShimmer()
        Spacer(Modifier.height(2.dp))
        repeat(3) { index ->
            val width = when (index) {
                0 -> Modifier.fillMaxWidth()
                1 -> Modifier.fillMaxWidth(0.95f)
                else -> Modifier.fillMaxWidth(0.6f)
            }
            Box(
                modifier = width
                    .height(14.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(AppColors.BackgroundSecondary),
            )
        }
    }
}

@Composable
private fun VersionsSectionShimmer(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        SectionTitleShimmer()
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ShimmerBlock(width = 76.dp, height = 32.dp, shape = CircleShape)
            ShimmerBlock(width = 60.dp, height = 32.dp, shape = CircleShape)
            ShimmerBlock(width = 92.dp, height = 32.dp, shape = CircleShape)
            ShimmerBlock(width = 70.dp, height = 32.dp, shape = CircleShape)
        }
    }
}

@Composable
private fun FilesSectionShimmer(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        SectionTitleShimmer()
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(2) { FileCardShimmer() }
        }
    }
}

@Composable
private fun FileCardShimmer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(AppColors.BackgroundSecondary)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(AppColors.Shimmer),
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ShimmerBlock(
                width = 180.dp,
                height = 14.dp,
                shape = RoundedCornerShape(6.dp),
            )
            ShimmerBlock(
                width = 70.dp,
                height = 12.dp,
                shape = RoundedCornerShape(6.dp),
            )
        }
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(AppColors.Shimmer),
        )
    }
}

@Composable
private fun SectionTitleShimmer() {
    ShimmerBlock(
        width = 160.dp,
        height = 20.dp,
        shape = RoundedCornerShape(6.dp),
    )
}

@Composable
private fun ShimmerBlock(
    width: Dp,
    height: Dp,
    shape: Shape,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(width = width, height = height)
            .clip(shape)
            .background(AppColors.BackgroundSecondary),
    )
}
