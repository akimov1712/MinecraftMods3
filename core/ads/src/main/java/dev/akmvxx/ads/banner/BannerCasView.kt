package dev.akmvxx.ads.banner

import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.cleversolutions.ads.android.CASBannerView
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R as UiR

@Composable
internal fun BannerCasView(modifier: Modifier = Modifier) {
    var bannerView by remember { mutableStateOf<CASBannerView?>(null) }

    LaunchedEffect(Unit) {
        bannerView = BannerCasPreloader.pop()
    }

    val heightModifier =
        if (bannerView != null) Modifier.wrapContentHeight()
        else Modifier.height(0.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(heightModifier)
            .clip(RoundedCornerShape(24.dp))
            .border(2.dp, AppColors.Primary, RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        bannerView?.let { view ->
            AndroidView(
                factory = {
                    (view.parent as? ViewGroup)?.removeView(view)
                    view
                }
            )

            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopStart)
                    .background(Color.Black.copy(0.4f), RoundedCornerShape(16.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                text = stringResource(UiR.string.ad),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose { bannerView?.destroy() }
    }
}
