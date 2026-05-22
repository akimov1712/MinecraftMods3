package dev.akmvxx.feature.files.ui

import android.text.format.Formatter
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.akmvxx.feature.files.FileItemUi
import dev.akmvxx.feature.files.FileStatus
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R
import dev.akmvxx.ui.utils.onClick

@Composable
internal fun FileRow(
    item: FileItemUi,
    onStartDownload: () -> Unit,
    onCancelDownload: () -> Unit,
    onInstall: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(AppColors.BackgroundSecondary)
            .border(width = 1.dp, color = AppColors.Outlined, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            LeadingIcon(status = item.status)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = item.name,
                    color = AppColors.TextWhite,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = formatSize(item.sizeBytes, item.status),
                    color = AppColors.TextWhite.copy(alpha = 0.55f),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            ActionSlot(
                status = item.status,
                onStartDownload = onStartDownload,
                onCancelDownload = onCancelDownload,
                onInstall = onInstall,
            )
        }


            if (item.status is FileStatus.Downloading) {
                ProgressBlock(status = item.status)
            }
        
    }
}

@Composable
private fun LeadingIcon(status: FileStatus) {
    val downloaded = status is FileStatus.Downloaded
    val bg = if (downloaded) AppColors.Green.copy(alpha = 0.18f)
    else AppColors.BackgroundPrimary
    val tint = if (downloaded) AppColors.Green
    else AppColors.TextWhite.copy(alpha = 0.65f)

    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(bg),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = if (downloaded) Icons.Filled.Check else Icons.Filled.Inventory2,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(22.dp),
        )
    }
}

@Composable
private fun ActionSlot(
    status: FileStatus,
    onStartDownload: () -> Unit,
    onCancelDownload: () -> Unit,
    onInstall: () -> Unit,
) {
    when (status) {
        FileStatus.Idle -> ActionPill(
            text = stringResource(R.string.files_get),
            icon = Icons.Filled.Download,
            background = AppColors.Primary,
            content = AppColors.White,
            onClick = onStartDownload,
        )
        is FileStatus.Downloading -> CancelButton(onClick = onCancelDownload)
        FileStatus.Downloaded -> ActionPill(
            text = stringResource(R.string.files_install),
            icon = Icons.Filled.PlayArrow,
            background = AppColors.Green,
            content = AppColors.White,
            onClick = onInstall,
        )
    }
}

@Composable
private fun ActionPill(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    background: Color,
    content: Color,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .height(40.dp)
            .clip(CircleShape)
            .background(background)
            .onClick { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = content,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = text,
            color = content,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
        )
    }
}

@Composable
private fun CancelButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(AppColors.BackgroundPrimary)
            .border(width = 1.dp, color = AppColors.Outlined, shape = CircleShape)
            .onClick { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(R.string.files_cancel),
            tint = AppColors.TextWhite.copy(alpha = 0.8f),
            modifier = Modifier.size(18.dp),
        )
    }
}

@Composable
private fun ProgressBlock(status: FileStatus.Downloading) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        LinearProgressIndicator(
            progress = { status.progressFraction.coerceAtLeast(0.001f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape),
            color = AppColors.Primary,
            trackColor = AppColors.BackgroundPrimary,
            strokeCap = StrokeCap.Round,
            drawStopIndicator = {},
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.files_downloading),
                color = AppColors.TextWhite.copy(alpha = 0.55f),
                fontSize = 12.sp,
            )
            Text(
                text = stringResource(R.string.files_progress_percent_format, status.progressPercent),
                color = AppColors.TextWhite.copy(alpha = 0.8f),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun formatSize(bytes: Long?, status: FileStatus): String {
    val ctx = LocalContext.current
    val sizeText = if (bytes != null && bytes > 0L) Formatter.formatShortFileSize(ctx, bytes) else "—"
    return when (status) {
        is FileStatus.Downloading -> if (status.hasKnownTotal) {
            val downloaded = Formatter.formatShortFileSize(ctx, status.downloadedBytes)
            val total = Formatter.formatShortFileSize(ctx, status.totalBytes)
            "$downloaded / $total"
        } else sizeText
        else -> sizeText
    }
}

