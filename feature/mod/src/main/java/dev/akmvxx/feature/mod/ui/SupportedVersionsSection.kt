package dev.akmvxx.feature.mod.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R

@Composable
internal fun SupportedVersionsSection(
    versions: List<String>,
    modifier: Modifier = Modifier,
) {
    if (versions.isEmpty()) return

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.mod_section_supported_versions),
                color = AppColors.TextWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
            )
            Text(
                text = pluralStringResource(R.plurals.mod_versions_count, versions.size, versions.size),
                color = AppColors.TextWhite.copy(alpha = 0.45f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
            )
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            versions.forEach { version ->
                VersionChip(version = version)
            }
        }
    }
}

@Composable
private fun VersionChip(version: String) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(AppColors.BackgroundSecondary)
            .border(width = 1.dp, color = AppColors.Outlined, shape = CircleShape)
            .padding(horizontal = 14.dp, vertical = 8.dp),
    ) {
        Text(
            text = version,
            color = AppColors.TextWhite,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
