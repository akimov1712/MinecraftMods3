package dev.akmvxx.feature.mod.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.akmvxx.domain.entity.mod.ModCategory
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R

@Composable
internal fun CategoryPill(
    category: ModCategory,
    modifier: Modifier = Modifier,
) {
    val accent = category.accentColor()

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(accent.copy(alpha = 0.16f))
            .border(width = 1.dp, color = accent.copy(alpha = 0.35f), shape = CircleShape)
            .padding(horizontal = 14.dp, vertical = 6.dp),
    ) {
        Text(
            text = stringResource(category.titleRes()),
            color = accent,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

private fun ModCategory.titleRes(): Int = when (this) {
    ModCategory.Addon -> R.string.mod_category_addon
    ModCategory.Maps -> R.string.mod_category_maps
    ModCategory.Texture -> R.string.mod_category_texture
    ModCategory.Skin -> R.string.mod_category_skins
}

private fun ModCategory.accentColor(): Color = when (this) {
    ModCategory.Addon -> AppColors.Primary
    ModCategory.Maps -> AppColors.Green
    ModCategory.Texture -> AppColors.CategoryPurple
    ModCategory.Skin -> AppColors.CategoryPink
}
