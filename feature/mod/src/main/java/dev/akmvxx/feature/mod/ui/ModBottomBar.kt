package dev.akmvxx.feature.mod.ui

import android.text.format.Formatter
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R
import dev.akmvxx.ui.components.AppButton
import dev.akmvxx.ui.utils.onClick

@Composable
internal fun ModBottomBar(
    isFavorite: Boolean,
    onDownload: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier,
    fileSizeBytes: Long? = null,
) {
    val context = LocalContext.current
    val downloadText = if (fileSizeBytes != null && fileSizeBytes > 0L) {
        val formatted = Formatter.formatShortFileSize(context, fileSizeBytes)
        stringResource(R.string.mod_download_with_size, formatted)
    } else {
        stringResource(R.string.mod_download)
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppButton(
            text = downloadText,
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            startIcon = {
                Icon(
                    imageVector = Icons.Filled.Download,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
            },
            onClick = onDownload,
        )

        FavoriteCircle(
            isFavorite = isFavorite,
            onClick = onFavoriteToggle,
        )
    }
}

@Composable
private fun FavoriteCircle(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(AppColors.BackgroundSecondary)
            .border(width = 1.dp, color = AppColors.Outlined, shape = CircleShape)
            .onClick { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = stringResource(R.string.mod_favorite_toggle),
            tint = if (isFavorite) AppColors.Primary else AppColors.TextWhite.copy(alpha = 0.7f),
            modifier = Modifier.size(22.dp),
        )
    }
}
