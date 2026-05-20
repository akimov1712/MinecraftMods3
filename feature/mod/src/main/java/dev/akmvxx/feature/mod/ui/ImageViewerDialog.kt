package dev.akmvxx.feature.mod.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R
import dev.akmvxx.ui.components.AppAsyncImage
import dev.akmvxx.ui.utils.onClick
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@Composable
internal fun ImageViewerDialog(
    images: List<String>,
    startIndex: Int,
    onDismiss: () -> Unit,
) {
    if (images.isEmpty()) return

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
        ),
    ) {
        val pagerState = rememberPagerState(
            initialPage = startIndex.coerceIn(0, images.lastIndex),
            pageCount = { images.size },
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                val zoomState = rememberZoomState()
                AppAsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .zoomable(zoomState),
                    url = images[page],
                    contentScale = ContentScale.Fit,
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(16.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.55f))
                    .border(width = 1.dp, color = Color.White.copy(alpha = 0.12f), shape = CircleShape)
                    .onClick { onDismiss() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.mod_close),
                    tint = AppColors.White,
                    modifier = Modifier.size(20.dp),
                )
            }

            if (images.size > 1) {
                Text(
                    text = stringResource(
                        R.string.mod_image_indicator,
                        pagerState.currentPage + 1,
                        images.size,
                    ),
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .statusBarsPadding()
                        .padding(top = 24.dp),
                )
            }
        }
    }
}
