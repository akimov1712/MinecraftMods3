package dev.akmvxx.feature.mod.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R

private const val COLLAPSED_MAX_LINES = 4

@Composable
internal fun ModDescription(
    text: String,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var isOverflowing by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.animateContentSize(animationSpec = tween(durationMillis = 250)),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        SectionLabel(text = stringResource(R.string.mod_section_about))

        Text(
            text = text,
            color = AppColors.TextWhite.copy(alpha = 0.85f),
            fontSize = 15.sp,
            lineHeight = 22.sp,
            maxLines = if (expanded) Int.MAX_VALUE else COLLAPSED_MAX_LINES,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { layoutResult ->
                if (!expanded) {
                    isOverflowing = layoutResult.hasVisualOverflow
                }
            },
        )

        if (isOverflowing || expanded) {
            ShowMoreToggle(
                expanded = expanded,
                onClick = { expanded = !expanded },
            )
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        color = AppColors.TextWhite,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
    )
}

@Composable
private fun ShowMoreToggle(
    expanded: Boolean,
    onClick: () -> Unit,
) {
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "chevron",
    )
    Row(
        modifier = Modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(
                if (expanded) R.string.mod_show_less else R.string.mod_show_more,
            ),
            color = AppColors.Primary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.width(2.dp))
        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = null,
            tint = AppColors.Primary,
            modifier = Modifier
                .size(18.dp)
                .rotate(rotation),
        )
    }
}
