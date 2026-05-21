package dev.akmvxx.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.akmvxx.ui.AppColors

@Composable
fun Modifier.onClick(onClick: () -> Unit): Modifier {
    val interaction = remember{ MutableInteractionSource() }
    return this.clickable(
        interactionSource = interaction,
        indication = ripple(color = AppColors.White),
        onClick = onClick
    )
}