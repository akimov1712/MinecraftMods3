package dev.akmvxx.feature.mod.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R

@Composable
internal fun ActionsRow(
    onHowToInstall: () -> Unit,
    onNotWorking: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        ActionPill(
            icon = Icons.Filled.HelpOutline,
            text = stringResource(R.string.mod_how_to_install),
            onClick = onHowToInstall,
            modifier = Modifier.weight(1f),
        )
        ActionPill(
            icon = Icons.Filled.WarningAmber,
            text = stringResource(R.string.mod_not_working),
            onClick = onNotWorking,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun ActionPill(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(AppColors.BackgroundSecondary)
            .border(width = 1.dp, color = AppColors.Outlined, shape = CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppColors.TextWhite.copy(alpha = 0.75f),
            modifier = Modifier.size(18.dp),
        )
        Text(
            text = text,
            color = AppColors.TextWhite,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
        )
    }
}
