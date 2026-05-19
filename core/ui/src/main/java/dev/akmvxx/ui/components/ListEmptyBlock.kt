package dev.akmvxx.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R

@Composable
fun ListEmptyBlock(
    modifier: Modifier = Modifier,
    message: String? = null,
    iconRes: ImageVector = Icons.Default.Search,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(AppColors.BackgroundSecondary)
            .padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(AppColors.Primary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = iconRes,
                contentDescription = null,
                tint = AppColors.Primary
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.the_list_is_empty),
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            lineHeight = 27.sp,
            color = AppColors.TextWhite,
            textAlign = TextAlign.Center
        )
        if (message != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = message,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                lineHeight = 20.sp,
                color = AppColors.TextWhite.copy(0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}
