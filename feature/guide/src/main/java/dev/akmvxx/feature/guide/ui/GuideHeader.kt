package dev.akmvxx.feature.guide.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R
import dev.akmvxx.ui.utils.onClick

@Composable
internal fun GuideHeader(
    title: String,
    subtitle: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AppColors.BackgroundSecondary)
                    .border(
                        width = 1.dp,
                        color = AppColors.Outlined,
                        shape = CircleShape,
                    )
                    .onClick { onBack() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.guide_back),
                    tint = AppColors.TextWhite,
                    modifier = Modifier.size(20.dp),
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = title,
            color = AppColors.TextWhite,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp,
            lineHeight = 34.sp,
            letterSpacing = (-0.5).sp,
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = subtitle,
            color = AppColors.TextWhite.copy(alpha = 0.6f),
            fontSize = 15.sp,
            lineHeight = 20.sp,
        )
    }
}
