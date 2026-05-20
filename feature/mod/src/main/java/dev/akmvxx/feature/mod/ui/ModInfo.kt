package dev.akmvxx.feature.mod.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.akmvxx.domain.entity.mod.ModCategory
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R
import dev.akmvxx.ui.entity.ModCategoryUi

@Composable
internal fun ModInfo(
    title: String,
    category: ModCategory,
    supportVersions: List<String>,
    fileCount: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            CategoryPill(category = ModCategoryUi.fromModCategory(category))
            Text(
                text = title,
                color = AppColors.TextWhite,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                lineHeight = 32.sp,
                letterSpacing = (-0.5).sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        }

        MetaRow(fileCount = fileCount, versionCount = supportVersions.size)

        if (supportVersions.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionLabel(text = stringResource(R.string.mod_section_compatible))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    supportVersions.forEach { version ->
                        VersionChip(version = version)
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryPill(category: ModCategoryUi) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(AppColors.Primary.copy(alpha = 0.18f))
            .border(width = 1.dp, color = AppColors.Primary.copy(alpha = 0.35f), shape = CircleShape)
            .padding(horizontal = 12.dp, vertical = 5.dp),
    ) {
        Text(
            text = stringResource(category.titleRes),
            color = AppColors.Primary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun MetaRow(fileCount: Int, versionCount: Int) {
    val files = pluralStringResource(R.plurals.files, fileCount, fileCount)
    val versions = pluralStringResource(R.plurals.mod_versions_count, versionCount, versionCount)
    val separator = stringResource(R.string.mod_meta_separator)
    val text = if (versionCount > 0) "$files$separator$versions" else files

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .background(AppColors.BackgroundSecondary)
            .border(width = 1.dp, color = AppColors.Outlined, shape = CircleShape)
            .padding(horizontal = 18.dp, vertical = 12.dp),
    ) {
        Text(
            text = text,
            color = AppColors.TextWhite.copy(alpha = 0.85f),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        color = AppColors.TextWhite.copy(alpha = 0.45f),
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.2.sp,
    )
}

@Composable
private fun VersionChip(version: String) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(AppColors.BackgroundSecondary)
            .border(width = 1.dp, color = AppColors.Outlined, shape = CircleShape)
            .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        Text(
            text = version,
            color = AppColors.TextWhite,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
