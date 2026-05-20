package dev.akmvxx.feature.mod

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.akmvxx.domain.entity.mod.ModEntity
import dev.akmvxx.feature.mod.ui.ModDescription
import dev.akmvxx.feature.mod.ui.ModGallery
import dev.akmvxx.feature.mod.ui.ModHeader
import dev.akmvxx.feature.mod.ui.ModInfo
import dev.akmvxx.navigation.rootNavigator
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R
import dev.akmvxx.ui.components.AppButton
import dev.akmvxx.ui.entity.ScreenUiState

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
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            ModHeader(
                imageUrl = mod.imageUrl,
                isFavorite = mod.isFavorite,
                onBack = onBack,
                onFavoriteToggle = { onIntent(ModIntent.ToggleFavorite) },
            )
            ModInfo(
                title = mod.title,
                category = mod.category,
                supportVersions = mod.supportVersions,
                fileCount = mod.downloadableFiles.size,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 4.dp),
            )
            if (mod.aboutModMessage.isNotBlank()) {
                Spacer(Modifier.height(24.dp))
                ModDescription(
                    text = mod.aboutModMessage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                )
            }
            if (mod.gallery.isNotEmpty()) {
                Spacer(Modifier.height(24.dp))
                ModGallery(
                    images = mod.gallery,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Spacer(Modifier.height(140.dp))
        }

        DownloadBar(
            onClick = { onIntent(ModIntent.Download) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
        )
    }
}

@Composable
private fun DownloadBar(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppButton(
        text = stringResource(R.string.mod_download),
        modifier = modifier.height(56.dp),
        startIcon = {
            Icon(
                imageVector = Icons.Filled.Download,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
        },
        onClick = onClick,
    )
}
