package dev.akmvxx.feature.favorite.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R

@Composable
internal fun FavoriteHeader(
    count: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = stringResource(R.string.favorite_title),
            color = AppColors.TextWhite,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 32.sp,
            letterSpacing = (-0.5).sp,
        )
        Text(
            text = pluralStringResource(R.plurals.favorite_count, count, count),
            color = AppColors.TextWhite.copy(alpha = 0.6f),
            fontSize = 15.sp,
        )
    }
}
