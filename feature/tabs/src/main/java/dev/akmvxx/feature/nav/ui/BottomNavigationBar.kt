package dev.akmvxx.feature.nav.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
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
internal fun BoxScope.BottomNavigationBar(){
    val navigator = tabsNavigator()
    Row(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .systemBarsPadding()
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(AppColors.BackgroundSecondary)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val tabs = Tabs.entries
        tabs.forEach {
            BottomNavigationBarItem(
                title = stringResource(it.titleRes),
                icon = painterResource(it.iconRes),
                isSelected = navigator.current == it.navKey
            ) {
                navigator.pushOnTop(it.navKey)
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
    val bgColor = if (isSelected) AppColors.White else Color.Transparent
    val textColor = if (isSelected) AppColors.Black else AppColors.White
    Row(
        modifier = Modifier.weight(1f)
            .clip(RoundedCornerShape(100.dp))
            .background(bgColor)
            .onClick{ onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = icon,
            contentDescription = title,
            tint = textColor
        )
        Spacer(Modifier.width(4.dp))

        val widthModifier = if (isSelected) Modifier.wrapContentWidth() else Modifier.width(0.dp)
        Text(
            modifier = Modifier.animateContentSize().then(widthModifier),
            text = title,
            fontWeight = FontWeight.Medium,
            color = textColor,
            fontSize = 14.sp,
            maxLines = 1
        )
    }
}
