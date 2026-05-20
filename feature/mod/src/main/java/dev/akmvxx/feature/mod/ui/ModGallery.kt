package dev.akmvxx.feature.mod.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R
import dev.akmvxx.ui.components.AppAsyncImage

@Composable
internal fun ModGallery(
    images: List<String>,
    modifier: Modifier = Modifier,
    horizontalContentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.mod_section_gallery).uppercase(),
            modifier = Modifier.padding(horizontal = 16.dp),
            color = AppColors.TextWhite.copy(alpha = 0.45f),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.2.sp,
        )
        LazyRow(
            contentPadding = horizontalContentPadding,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(items = images, key = { it }) { url ->
                AppAsyncImage(
                    modifier = Modifier
                        .size(width = 220.dp, height = 140.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    url = url,
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}
