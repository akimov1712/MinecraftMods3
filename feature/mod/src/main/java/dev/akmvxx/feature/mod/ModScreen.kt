package dev.akmvxx.feature.mod

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import dev.akmvxx.feature.mod.ui.ModScreenShimmer
import dev.akmvxx.feature.mod.ui.ReportDialog
import dev.akmvxx.feature.mod.ui.StickyToolbar
import dev.akmvxx.feature.mod.ui.SupportedVersionsSection
import dev.akmvxx.navigation.RootNavKey
import dev.akmvxx.navigation.rootNavigator
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R
import dev.akmvxx.ui.components.AppPullRefresh
import dev.akmvxx.ui.components.ListErrorBlock
import dev.akmvxx.ui.entity.ScreenUiState
import dev.akmvxx.ui.utils.onClick

private val HorizontalPadding = 20.dp
private val StickyHeaderTriggerDp = 220.dp

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
        onOpenFiles = { navigator.push(RootNavKey.Files(modId = modId)) },
        onIntent = viewModel::sendIntent,
    )
}

@Composable
private fun ModContent(
    state: ModState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onOpenFiles: () -> Unit,
    onIntent: (ModIntent) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.BackgroundPrimary),
    ) {
        when (val status = state.status) {
            is ScreenUiState.Loading -> LoadingState(onBack = onBack)
            is ScreenUiState.Error -> ErrorState(
                message = status.message,
                onBack = onBack,
                onRetry = onRetry,
            )
            is ScreenUiState.Success -> {
                state.mod?.let { mod ->
                    SuccessState(
                        mod = mod,
                        fileSizeBytes = state.fileSizeBytes,
                        onBack = onBack,
                        onRefresh = onRetry,
                        onOpenFiles = onOpenFiles,
                        onIntent = onIntent,
                    )
                }
            }
            else -> Unit
        }
    }
}

@Composable
private fun LoadingState(onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        ModScreenShimmer()
        BackButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    onBack: () -> Unit,
    onRetry: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = HorizontalPadding),
            contentAlignment = Alignment.Center,
        ) {
            ListErrorBlock(
                title = stringResource(R.string.error_title_pagination),
                message = message,
                onClickRetry = onRetry,
            )
        }
        BackButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
        )
    }
}

@Composable
private fun SuccessState(
    mod: ModEntity,
    fileSizeBytes: Long?,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onOpenFiles: () -> Unit,
    onIntent: (ModIntent) -> Unit,
) {
    val images = remember(mod.id, mod.imageUrl, mod.gallery) {
        buildList {
            if (mod.imageUrl.isNotBlank()) add(mod.imageUrl)
            addAll(mod.gallery)
        }
    }

    var viewerStartIndex by remember { mutableStateOf<Int?>(null) }
    var reportDialogVisible by remember { mutableStateOf(false) }
    val accent = mod.category.accentColor()
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val stickyHeaderTriggerPx = remember(density) {
        with(density) { StickyHeaderTriggerDp.toPx() }
    }
    val showStickyHeader by remember {
        derivedStateOf { scrollState.value > stickyHeaderTriggerPx }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AppPullRefresh(
            modifier = Modifier.fillMaxSize(),
            onRefresh = onRefresh,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
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

                Spacer(Modifier.height(16.dp))

                ActionsRow(
                    onHowToInstall = {},
                    onNotWorking = { reportDialogVisible = true },
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
                        category = mod.category,
                        accent = accent,
                        modifier = Modifier.padding(horizontal = HorizontalPadding),
                        onFileClick = { onOpenFiles() },
                    )
                }

                Spacer(Modifier.height(140.dp))
            }
        }

        ModBottomBar(
            isFavorite = mod.isFavorite,
            onDownload = onOpenFiles,
            onFavoriteToggle = { onIntent(ModIntent.ToggleFavorite) },
            fileSizeBytes = fileSizeBytes,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = HorizontalPadding, vertical = 16.dp),
        )

        StickyToolbar(
            visible = showStickyHeader,
            title = mod.title,
            onBack = onBack,
            modifier = Modifier.align(Alignment.TopCenter),
        )

        viewerStartIndex?.let { startIndex ->
            ImageViewerDialog(
                images = images,
                startIndex = startIndex,
                onDismiss = { viewerStartIndex = null },
            )
        }

        if (reportDialogVisible) {
            ReportDialog(
                modTitle = mod.title,
                onDismiss = { reportDialogVisible = false },
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
