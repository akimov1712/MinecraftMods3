package dev.akmvxx.feature.nav.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
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

@Composable
internal fun BoxScope.BottomNavigationBar() {
    val navigator = tabsNavigator()
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .systemBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .fillMaxWidth()
            .dropShadow(
                shape = RoundedCornerShape(28.dp),
                shadow = Shadow(
                    radius = 16.dp,
                    color = AppColors.Black.copy(alpha = 0.35f)
                )
            )
            .clip(RoundedCornerShape(28.dp))
            .background(AppColors.BackgroundSecondary)
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
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
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) AppColors.Primary else Color.Transparent,
        animationSpec = animationSpec,
        label = "bg"
    )
    val contentColor by animateColorAsState(
        targetValue = when {
            isSelected -> AppColors.TextWhite
            else -> AppColors.TextWhite.copy(alpha = 0.55f)
        },
        animationSpec = animationSpec,
        label = "content"
    )

    Row(
        modifier = Modifier
            .then(if (isSelected) Modifier.weight(1f) else Modifier)
            .height(48.dp)
            .defaultMinSize(minWidth = 48.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .onClick { onClick() }
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = icon,
            contentDescription = title,
            tint = contentColor
        )
        AnimatedVisibility(
            visible = isSelected,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(Modifier.width(8.dp))
                Text(
                    text = title,
                    color = contentColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
            }
        }
    }
}
