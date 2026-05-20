package dev.akmvxx.feature.mod.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R

@Composable
internal fun FilesSection(
    files: List<String>,
    accent: Color,
    modifier: Modifier = Modifier,
) {
    if (files.isEmpty()) return

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
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
                fontSize = 17.sp,
            )
            Text(
                text = files.size.toString(),
                color = AppColors.TextWhite.copy(alpha = 0.45f),
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            files.forEach { fileName ->
                FileCard(
                    name = fileName,
                    subtitle = extractVersion(fileName),
                    accent = accent,
                )
            }
        }
    }
}

private val VERSION_REGEX = Regex("""(\d+\.\d+(?:\.\d+)?)""")

private fun extractVersion(fileName: String): String? {
    return VERSION_REGEX.find(fileName)?.value
}
