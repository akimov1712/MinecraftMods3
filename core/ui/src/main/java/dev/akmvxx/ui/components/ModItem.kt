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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.akmvxx.domain.entity.mod.ModEntity
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R
import dev.akmvxx.ui.entity.ModCategoryUi
import dev.akmvxx.ui.utils.onClick

@Composable
fun ModItem(mod: ModEntity, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.1f)
            .dropShadow(
                shape = RoundedCornerShape(28.dp),
                shadow = Shadow(
                    radius = 16.dp,
                    color = AppColors.Black.copy(alpha = 0.4f)
                )
            )
            .clip(RoundedCornerShape(28.dp))
            .background(AppColors.BackgroundSecondary)
            .onClick { onClick() }
    ) {
        AppAsyncImage(
            modifier = Modifier.fillMaxSize(),
            url = mod.imageUrl,
            contentScale = ContentScale.Crop
        )
        BottomGradient()
        FileCountChip(fileCount = mod.downloadableFiles.size)
        if (mod.isFavorite) FavoriteBadge()
        InfoBlock(mod = mod, onDownloadClick = onClick)
        InnerHighlight()
    }
}

@Composable
private fun BoxScope.BottomGradient() {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height(180.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        AppColors.Black.copy(alpha = 0.45f),
                        AppColors.Black.copy(alpha = 0.85f),
                    )
                )
            )
    )
}

@Composable
private fun InnerHighlight() {
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

@Composable
private fun BoxScope.FileCountChip(fileCount: Int) {
    Row(
        modifier = Modifier
            .align(Alignment.TopStart)
            .padding(12.dp)
            .clip(CircleShape)
            .background(AppColors.Black.copy(alpha = 0.55f))
            .border(
                width = 1.dp,
                color = AppColors.White.copy(alpha = 0.12f),
                shape = CircleShape
            )
            .padding(start = 4.dp, top = 4.dp, bottom = 4.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(AppColors.Primary)
                .padding(5.dp),
            imageVector = Icons.Default.Download,
            contentDescription = null,
            tint = AppColors.White
        )
        Text(
            text = pluralStringResource(R.plurals.files, fileCount, fileCount),
            color = AppColors.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun BoxScope.FavoriteBadge() {
    Box(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(12.dp)
            .size(32.dp)
            .clip(CircleShape)
            .background(AppColors.Black.copy(alpha = 0.55f))
            .border(
                width = 1.dp,
                color = AppColors.White.copy(alpha = 0.12f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = AppColors.Primary
        )
    }
}

@Composable
private fun BoxScope.InfoBlock(
    mod: ModEntity,
    onDownloadClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CategoryPill(category = mod.let { ModCategoryUi.fromModCategory(it.category) })
            Text(
                text = mod.title,
                color = AppColors.White,
                fontSize = 18.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(Modifier.width(12.dp))
        DownloadButton(onClick = onDownloadClick)
    }
}

@Composable
private fun CategoryPill(category: ModCategoryUi) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(AppColors.White.copy(alpha = 0.15f))
            .border(
                width = 1.dp,
                color = AppColors.White.copy(alpha = 0.18f),
                shape = CircleShape
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = stringResource(category.titleRes),
            color = AppColors.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

@Composable
private fun DownloadButton(onClick: () -> Unit) {
    Icon(
        modifier = Modifier
            .size(48.dp)
            .dropShadow(
                shape = CircleShape,
                shadow = Shadow(
                    radius = 12.dp,
                    color = AppColors.Primary.copy(alpha = 0.6f)
                )
            )
            .clip(CircleShape)
            .background(AppColors.Primary)
            .onClick { onClick() }
            .padding(13.dp),
        imageVector = Icons.Default.Download,
        contentDescription = null,
        tint = AppColors.White
    )
}
