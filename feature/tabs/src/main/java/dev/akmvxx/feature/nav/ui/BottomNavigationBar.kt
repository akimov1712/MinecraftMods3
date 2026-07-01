package dev.akmvxx.feature.nav.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
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

private val BarShape = RoundedCornerShape(26.dp)
private val IndicatorShape = RoundedCornerShape(16.dp)

@Composable
internal fun BoxScope.BottomNavigationBar() {
    val navigator = tabsNavigator()
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .systemBarsPadding()
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .fillMaxWidth()
            .dropShadow(
                shape = BarShape,
                shadow = Shadow(
                    radius = 20.dp,
                    color = AppColors.Black.copy(alpha = 0.5f)
                )
            )
            .clip(BarShape)
            .background(AppColors.BackgroundSecondary)
            .border(width = 1.dp, color = AppColors.White.copy(alpha = 0.06f), shape = BarShape)
            .padding(horizontal = 6.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
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
    val animationSpec = spring<Color>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow
    )
    val indicatorColor by animateColorAsState(
        targetValue = if (isSelected) AppColors.Primary.copy(alpha = 0.16f) else Color.Transparent,
        animationSpec = animationSpec,
        label = "indicator"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) AppColors.Primary else AppColors.TextWhite.copy(alpha = 0.5f),
        animationSpec = animationSpec,
        label = "content"
    )

    Column(
        modifier = Modifier
            .weight(1f)
            .clip(IndicatorShape)
            .onClick { onClick() }
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(width = 44.dp, height = 32.dp)
                .clip(CircleShape)
                .background(indicatorColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = icon,
                contentDescription = title,
                tint = contentColor
            )
        }
        Spacer(Modifier.height(5.dp))
        Text(
            text = title,
            color = contentColor,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            maxLines = 1
        )
    }
}
