package dev.akmvxx.feature.mod

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.akmvxx.domain.entity.mod.ModCategory
import dev.akmvxx.domain.entity.mod.ModEntity
import dev.akmvxx.feature.mod.ui.ActionsRow
import dev.akmvxx.feature.mod.ui.CategoryPill
import dev.akmvxx.feature.mod.ui.FilesSection
import dev.akmvxx.feature.mod.ui.ImageViewerDialog
import dev.akmvxx.feature.mod.ui.ModBottomBar
import dev.akmvxx.feature.mod.ui.ModCarousel
import dev.akmvxx.feature.mod.ui.ModDescription
import dev.akmvxx.feature.mod.ui.SupportedVersionsSection
import dev.akmvxx.navigation.rootNavigator
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R
import dev.akmvxx.ui.components.AppButton
import dev.akmvxx.ui.entity.ScreenUiState
import dev.akmvxx.ui.utils.onClick

private val HorizontalPadding = 20.dp

@Composable
fun ModScreen(
    modId: Int,
    viewModel: ModViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navigator = rootNavigator()

    LaunchedEffect(modId) {
        viewModel.sendIntent(ModIntent.LoadMod(modId))
    }

    ModContent(
        state = state,
        onBack = { navigator.pop() },
        onRetry = { viewModel.sendIntent(ModIntent.LoadMod(modId)) },
        onIntent = viewModel::sendIntent,
    )
}

@Composable
private fun ModContent(
    state: ModState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onIntent: (ModIntent) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.BackgroundPrimary),
    ) {
        when (val status = state.status) {
            is ScreenUiState.Loading -> LoadingState()
            is ScreenUiState.Error -> ErrorState(
                message = status.message,
                onBack = onBack,
                onRetry = onRetry,
            )
            is ScreenUiState.Success -> {
                state.mod?.let { mod ->
                    SuccessState(
                        mod = mod,
                        onBack = onBack,
                        onIntent = onIntent,
                    )
                }
            }
            else -> Unit
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = AppColors.Primary,
            strokeWidth = 2.5.dp,
            modifier = Modifier.size(36.dp),
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    onBack: () -> Unit,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            color = AppColors.TextWhite.copy(alpha = 0.8f),
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(20.dp))
        AppButton(
            text = stringResource(R.string.retry),
            onClick = onRetry,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
        )
        Spacer(Modifier.height(10.dp))
        AppButton(
            text = stringResource(R.string.mod_back),
            onClick = onBack,
            containerColor = AppColors.BackgroundSecondary,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
        )
    }
}

@Composable
private fun SuccessState(
    mod: ModEntity,
    onBack: () -> Unit,
    onIntent: (ModIntent) -> Unit,
) {
    val images = remember(mod.id, mod.imageUrl, mod.gallery) {
        buildList {
            if (mod.imageUrl.isNotBlank()) add(mod.imageUrl)
            addAll(mod.gallery)
        }
    }

    var viewerStartIndex by remember { mutableStateOf<Int?>(null) }
    val accent = mod.category.accentColor()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                ModCarousel(
                    images = images,
                    onImageClick = { startIndex -> viewerStartIndex = startIndex },
                )
                BackButton(
                    onClick = onBack,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                )
            }

            Spacer(Modifier.height(8.dp))

            CategoryPill(
                category = mod.category,
                modifier = Modifier.padding(horizontal = HorizontalPadding),
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = mod.title,
                color = AppColors.TextWhite,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                lineHeight = 32.sp,
                letterSpacing = (-0.5).sp,
                modifier = Modifier.padding(horizontal = HorizontalPadding),
            )

            if (mod.aboutModMessage.isNotBlank()) {
                Spacer(Modifier.height(24.dp))
                ModDescription(
                    text = mod.aboutModMessage,
                    modifier = Modifier.padding(horizontal = HorizontalPadding),
                )
            }

            if (mod.supportVersions.isNotEmpty()) {
                Spacer(Modifier.height(28.dp))
                SupportedVersionsSection(
                    versions = mod.supportVersions,
                    modifier = Modifier.padding(horizontal = HorizontalPadding),
                )
            }

            if (mod.downloadableFiles.isNotEmpty()) {
                Spacer(Modifier.height(28.dp))
                FilesSection(
                    files = mod.downloadableFiles,
                    accent = accent,
                    modifier = Modifier.padding(horizontal = HorizontalPadding),
                )
            }

            Spacer(Modifier.height(20.dp))

            ActionsRow(
                onHowToInstall = {},
                onNotWorking = {},
                modifier = Modifier.padding(horizontal = HorizontalPadding),
            )

            Spacer(Modifier.height(140.dp))
        }

        ModBottomBar(
            isFavorite = mod.isFavorite,
            onDownload = { onIntent(ModIntent.Download) },
            onFavoriteToggle = { onIntent(ModIntent.ToggleFavorite) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = HorizontalPadding, vertical = 16.dp),
        )

        viewerStartIndex?.let { startIndex ->
            ImageViewerDialog(
                images = images,
                startIndex = startIndex,
                onDismiss = { viewerStartIndex = null },
            )
        }
    }
}

@Composable
private fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(AppColors.Black.copy(alpha = 0.55f))
            .border(width = 1.dp, color = AppColors.White.copy(alpha = 0.12f), shape = CircleShape)
            .onClick { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.mod_back),
            tint = AppColors.White,
            modifier = Modifier.size(20.dp),
        )
    }
}

private fun ModCategory.accentColor() = when (this) {
    ModCategory.Addon -> AppColors.Primary
    ModCategory.Maps -> AppColors.Green
    ModCategory.Texture -> AppColors.CategoryPurple
    ModCategory.Skin -> AppColors.CategoryPink
}
