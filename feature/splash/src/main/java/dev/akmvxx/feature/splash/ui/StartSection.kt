package dev.akmvxx.feature.splash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.akmvxx.ads.NativeCoordinator
import dev.akmvxx.navigation.RootNavKey
import dev.akmvxx.navigation.rootNavigator
import dev.akmvxx.ui.AppColors

@Preview
@Composable
internal fun StartSection() {
    Column(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(40.dp))
            .background(AppColors.BackgroundPrimary)
            .border(1.dp, AppColors.Outlined, RoundedCornerShape(40.dp))
            .padding(24.dp)
    ) {
        val navigator = rootNavigator()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Image(
                painter = painterResource(dev.akmvxx.ui.R.drawable.logo),
                contentDescription = "logo",
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AppColors.BackgroundSecondary)
                    .border(1.dp, AppColors.Outlined, RoundedCornerShape(12.dp))
                    .padding(2.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(dev.akmvxx.ui.R.string.app_name),
                fontWeight = FontWeight.SemiBold,
                color = AppColors.TextWhite,
                fontSize = 16.sp
            )
            Box(
                modifier = Modifier.size(36.dp)
                    .clip(CircleShape)
                    .background(AppColors.Primary.copy(0.2f)),
            ){
                Icon(
                    modifier = Modifier.fillMaxSize().padding(10.dp),
                    painter = painterResource(dev.akmvxx.ui.R.drawable.ic_check),
                    contentDescription = "check mark",
                    tint = AppColors.Primary
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        Button(
            modifier = Modifier.fillMaxWidth()
                .defaultMinSize(minHeight = 58.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary),
            onClick = {
                val destination =
                    if (NativeCoordinator.hasAd() && !NativeCoordinator.isSelectedBannerAdType()) {
                        RootNavKey.AdShow(next = RootNavKey.Tabs)
                    } else {
                        RootNavKey.Tabs
                    }
                navigator.replaceAll(destination)
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(dev.akmvxx.ui.R.drawable.ic_play),
                    contentDescription = "Get started",
                    tint = AppColors.TextWhite
                )
                Text(
                    text = stringResource(dev.akmvxx.ui.R.string.get_started),
                    color = AppColors.TextWhite,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        }
    }
}