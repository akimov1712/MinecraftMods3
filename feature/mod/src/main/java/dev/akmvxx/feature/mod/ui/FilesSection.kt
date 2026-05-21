package dev.akmvxx.feature.mod.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.akmvxx.common.extractFileNameOrFallback
import dev.akmvxx.common.extractFileVersion
import dev.akmvxx.domain.entity.mod.ModCategory
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R

@Composable
internal fun FilesSection(
    files: List<String>,
    category: ModCategory,
    accent: Color,
    modifier: Modifier = Modifier,
) {
    if (files.isEmpty()) return

    val extensionFallback = category.getExtensionFile()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.mod_section_files),
                color = AppColors.TextWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
            )
            Text(
                text = stringResource(R.string.mod_meta_separator),
                color = AppColors.TextWhite.copy(alpha = 0.45f),
                fontSize = 32.sp,
            )
            Text(
                text = files.size.toString(),
                color = AppColors.TextWhite.copy(alpha = 0.45f),
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            files.forEachIndexed { index, url ->
                val name = url.extractFileNameOrFallback(extensionFallback)
                    ?: stringResource(
                        R.string.mod_file_default_name,
                        index + 1,
                    ) + extensionFallback
                FileCard(
                    name = name,
                    subtitle = url.extractFileVersion(),
                    accent = accent,
                )
            }
        }
    }
}
