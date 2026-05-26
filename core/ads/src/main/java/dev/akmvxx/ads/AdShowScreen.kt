package dev.akmvxx.ads

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.akmvxx.ui.AppColors

@Composable
fun AdShowScreen(onClose: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(AppColors.BackgroundPrimary)
            .systemBarsPadding()
    ) {
        NativeAds.Show(slot = NativeAds.Slot.Fullscreen)

        Icon(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 40.dp, end = 20.dp)
                .size(28.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(onClick = onClose)
                .padding(6.dp),
            imageVector = Icons.Default.Close,
            contentDescription = null,
            tint = Color.White,
        )
    }
}
