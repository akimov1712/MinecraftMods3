package dev.akmvxx.feature.nav.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.akmvxx.feature.nav.Tabs
import dev.akmvxx.navigation.tabsNavigator
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.utils.onClick

private val BarShape = RoundedCornerShape(28.dp)

@Composable
internal fun BoxScope.BottomNavigationBar() {
    val navigator = tabsNavigator()
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .systemBarsPadding()
            .padding(horizontal = 22.dp, vertical = 14.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .dropShadow(
                    shape = BarShape,
                    shadow = Shadow(radius = 22.dp, color = AppColors.Black.copy(alpha = 0.55f))
                )
                .clip(BarShape)
                .background(AppColors.BackgroundSecondary)
                .border(width = 1.dp, color = AppColors.White.copy(alpha = 0.06f), shape = BarShape)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Tabs.entries.forEach { tab ->
                BottomNavigationBarItem(
                    title = stringResource(tab.titleRes),
                    icon = painterResource(tab.iconRes),
                    isSelected = navigator.current == tab.navKey,
                    onClick = { navigator.pushOnTop(tab.navKey) }
                )
            }
        }
    }
}

@Composable
private fun RowScope.BottomNavigationBarItem(
    title: String,
    icon: Painter,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val spec = spring<Color>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow
    )
    val lift by animateDpAsState(
        targetValue = if (isSelected) (-20).dp else 0.dp,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessLow),
        label = "lift"
    )
    val glowAlpha by animateFloatAsState(
        targetValue = if (isSelected) 0.65f else 0f,
        label = "glow"
    )
    val bubbleColor by animateColorAsState(
        targetValue = if (isSelected) AppColors.Primary else AppColors.BackgroundPrimary,
        animationSpec = spec,
        label = "bubble"
    )
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) AppColors.White else AppColors.TextWhite.copy(alpha = 0.45f),
        animationSpec = spec,
        label = "icon"
    )
    val labelColor by animateColorAsState(
        targetValue = if (isSelected) AppColors.Primary else Color.Transparent,
        animationSpec = spec,
        label = "label"
    )

    Column(
        modifier = Modifier
            .weight(1f)
            .onClick { onClick() }
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .offset(y = lift)
                .size(52.dp)
                .dropShadow(
                    shape = CircleShape,
                    shadow = Shadow(radius = 20.dp, color = AppColors.Primary.copy(alpha = glowAlpha))
                )
                .clip(CircleShape)
                .background(bubbleColor)
                .border(
                    width = 1.dp,
                    color = if (isSelected) AppColors.White.copy(alpha = 0.25f) else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(22.dp),
                painter = icon,
                contentDescription = title,
                tint = iconColor
            )
        }
        Spacer(Modifier.height(4.dp).offset(y = lift))
        Text(
            modifier = Modifier.offset(y = lift),
            text = title,
            color = labelColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}
