package dev.akmvxx.feature.browse.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R
import dev.akmvxx.ui.components.AppTextField
import dev.akmvxx.ui.entity.ModSortedUi

@Composable
internal fun BrowseSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    sortItems: List<ModSortedUi>,
    sortedIndex: Int,
    onSortSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AppTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            placeholder = stringResource(R.string.search_mods),
            leadingIcon = Icons.Filled.Search,
        )
        SortButton(
            items = sortItems,
            selectedIndex = sortedIndex,
            onSelected = onSortSelected,
        )
    }
}

@Composable
private fun SortButton(
    items: List<ModSortedUi>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(AppColors.Primary)
                .clickable { expanded = !expanded },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(22.dp),
                imageVector = Icons.Filled.Tune,
                contentDescription = null,
                tint = AppColors.White
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = AppColors.BackgroundSecondary,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(width = 1.dp, color = AppColors.Outlined),
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(item.titleRes),
                            color = if (isSelected) AppColors.Primary else AppColors.TextWhite,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        )
                    },
                    onClick = {
                        onSelected(index)
                        expanded = false
                    },
                    colors = MenuDefaults.itemColors(textColor = AppColors.TextWhite),
                )
            }
        }
    }
}
