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

private val CardShape = RoundedCornerShape(24.dp)
private val ImageShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)

@Composable
fun ModItem(mod: ModEntity, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .dropShadow(
                shape = CardShape,
                shadow = Shadow(
                    radius = 18.dp,
                    color = AppColors.Black.copy(alpha = 0.45f)
                )
            )
            .clip(CardShape)
            .background(AppColors.BackgroundSecondary)
            .border(width = 1.dp, color = AppColors.White.copy(alpha = 0.06f), shape = CardShape)
            .onClick { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 10f)
                .clip(ImageShape)
        ) {
            AppAsyncImage(
                modifier = Modifier.fillMaxSize(),
                url = mod.imageUrl,
                contentScale = ContentScale.Crop
            )
            CategoryChip(mod = mod)
            if (mod.isFavorite) FavoriteBadge()
        }
        ModCardBody(mod = mod, onDownloadClick = onClick)
    }
}

@Composable
private fun BoxScope.CategoryChip(mod: ModEntity) {
    val category = ModCategoryUi.fromModCategory(mod.category)
    Box(
        modifier = Modifier
            .align(Alignment.TopStart)
            .padding(12.dp)
            .clip(CircleShape)
            .background(AppColors.Black.copy(alpha = 0.55f))
            .border(width = 1.dp, color = AppColors.White.copy(alpha = 0.14f), shape = CircleShape)
            .padding(horizontal = 12.dp, vertical = 6.dp)
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
private fun BoxScope.FavoriteBadge() {
    Box(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(12.dp)
            .size(32.dp)
            .clip(CircleShape)
            .background(AppColors.Black.copy(alpha = 0.55f))
            .border(width = 1.dp, color = AppColors.White.copy(alpha = 0.14f), shape = CircleShape),
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
private fun ModCardBody(mod: ModEntity, onDownloadClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = mod.title,
            color = AppColors.White,
            fontSize = 17.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = pluralStringResource(
                    R.plurals.files,
                    mod.downloadableFiles.size,
                    mod.downloadableFiles.size
                ),
                color = AppColors.TextWhite.copy(alpha = 0.55f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.weight(1f))
            DownloadButton(onClick = onDownloadClick)
        }
    }
}

@Composable
private fun DownloadButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(AppColors.Primary)
            .onClick { onClick() }
            .padding(horizontal = 18.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            modifier = Modifier.size(18.dp),
            imageVector = Icons.Default.Download,
            contentDescription = null,
            tint = AppColors.White
        )
        Text(
            text = stringResource(R.string.mod_download),
            color = AppColors.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}
