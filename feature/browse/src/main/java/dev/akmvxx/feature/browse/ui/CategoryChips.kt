package dev.akmvxx.feature.browse.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.entity.ModCategoryUi
import dev.akmvxx.ui.utils.onClick
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.res.stringResource

@Composable
internal fun CategoryChips(
    categories: List<ModCategoryUi>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        itemsIndexed(categories) { index, category ->
            CategoryChip(
                title = stringResource(category.titleRes),
                isSelected = index == selectedIndex,
                onClick = { onSelected(index) }
            )
        }
    }
}

@Composable
private fun CategoryChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val background by animateColorAsState(
        targetValue = if (isSelected) AppColors.Primary else AppColors.BackgroundSecondary,
        label = "chipBg"
    )
    val content by animateColorAsState(
        targetValue = if (isSelected) AppColors.White else AppColors.TextWhite.copy(alpha = 0.6f),
        label = "chipContent"
    )
    val borderColor = if (isSelected) Color.Transparent else AppColors.Outlined

    Text(
        text = title,
        color = content,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .clip(CircleShape)
            .background(background)
            .border(width = 1.dp, color = borderColor, shape = CircleShape)
            .onClick { onClick() }
            .padding(horizontal = 18.dp, vertical = 10.dp)
    )
}
