package dev.akmvxx.feature.splash.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.akmvxx.ui.AppColors
import kotlin.math.abs

private const val DOT_COUNT = 4

@Composable
internal fun Loader(modifier: Modifier = Modifier, isLoading: Boolean){
    val transition = rememberInfiniteTransition(label = "loader")
    val activePosition by transition.animateFloat(
        initialValue = 0f,
        targetValue = (DOT_COUNT - 1).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "dot",
    )

    val animateVisibility by animateFloatAsState(if (isLoading) 1f else 0f)

    Column(
        modifier = modifier.alpha(animateVisibility),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(DOT_COUNT) { index ->
                val distance = abs(activePosition - index).coerceAtMost(1f)
                val intensity = 1f - distance
                val dotSize = (8f + 3f * intensity).dp
                Box(
                    modifier = Modifier
                        .size(dotSize)
                        .clip(CircleShape)
                        .background(AppColors.TextWhite.copy(alpha = 0.25f + 0.75f * intensity)),
                )
            }
        }
    }
}
