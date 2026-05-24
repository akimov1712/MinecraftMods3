package dev.akmvxx.feature.guide.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R

@Composable
internal fun StepCard(
    number: Int,
    title: String,
    text: String,
    @DrawableRes screenshotRes: Int?,
    isPlaceholder: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(AppColors.BackgroundSecondary)
            .border(width = 1.dp, color = AppColors.Outlined, shape = RoundedCornerShape(20.dp))
            .padding(18.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            StepBadge(number = number)

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = AppColors.TextWhite,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = text,
                    color = AppColors.TextWhite.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                )
            }
        }

        screenshotRes?.let {
            Spacer(Modifier.height(16.dp))
            Screenshot(
                number = number,
                screenshotRes = screenshotRes,
                isPlaceholder = isPlaceholder,
            )
        }
    }
}

@Composable
private fun StepBadge(number: Int) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(AppColors.Primary),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = number.toString(),
            color = AppColors.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
        )
    }
}

@Composable
private fun Screenshot(
    number: Int,
    @DrawableRes screenshotRes: Int,
    isPlaceholder: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(AppColors.BackgroundPrimary)
            .border(
                width = 1.dp,
                color = AppColors.Outlined,
                shape = RoundedCornerShape(14.dp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (isPlaceholder) {
            Icon(
                painter = painterResource(R.drawable.ic_image_placeholder),
                contentDescription = null,
                tint = AppColors.TextWhite.copy(alpha = 0.25f),
                modifier = Modifier.size(48.dp),
            )
        } else {
            Image(
                painter = painterResource(screenshotRes),
                contentDescription = stringResource(
                    R.string.guide_screenshot_content_description,
                    number,
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
