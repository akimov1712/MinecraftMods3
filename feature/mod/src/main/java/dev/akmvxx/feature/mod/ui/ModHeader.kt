package dev.akmvxx.feature.mod.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R
import dev.akmvxx.ui.components.AppAsyncImage
import dev.akmvxx.ui.utils.onClick

@Composable
internal fun ModHeader(
    imageUrl: String,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
    ) {
        AppAsyncImage(
            modifier = Modifier.fillMaxSize(),
            url = imageUrl,
            contentScale = ContentScale.Crop,
        )
        BottomFade()
        TopBar(
            isFavorite = isFavorite,
            onBack = onBack,
            onFavoriteToggle = onFavoriteToggle,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 12.dp),
        )
    }
}

@Composable
private fun BoxScope.BottomFade() {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height(120.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        AppColors.BackgroundPrimary,
                    ),
                ),
            ),
    )
}

@Composable
private fun TopBar(
    isFavorite: Boolean,
    onBack: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        CircleIconButton(
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.mod_back),
            tint = AppColors.White,
            onClick = onBack,
            modifier = Modifier.align(Alignment.CenterStart),
        )
        CircleIconButton(
            icon = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = stringResource(R.string.mod_favorite_toggle),
            tint = if (isFavorite) AppColors.Primary else AppColors.White,
            onClick = onFavoriteToggle,
            modifier = Modifier.align(Alignment.CenterEnd),
        )
    }
}

@Composable
private fun CircleIconButton(
    icon: ImageVector,
    contentDescription: String,
    tint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(AppColors.Black.copy(alpha = 0.55f))
            .border(
                width = 1.dp,
                color = AppColors.White.copy(alpha = 0.12f),
                shape = CircleShape,
            )
            .onClick { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(20.dp),
        )
    }
}

