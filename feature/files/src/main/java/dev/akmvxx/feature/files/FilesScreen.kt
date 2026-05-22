package dev.akmvxx.feature.files

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.akmvxx.feature.files.ui.FileRow
import dev.akmvxx.feature.files.ui.FilesHeader
import dev.akmvxx.navigation.rootNavigator
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R
import dev.akmvxx.ui.components.AppButton
import dev.akmvxx.ui.components.ListErrorBlock
import dev.akmvxx.ui.entity.ScreenUiState

private val HorizontalPadding = 20.dp

@Composable
fun FilesScreen(
    modId: Int,
    viewModel: FilesViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navigator = rootNavigator()

    LaunchedEffect(modId) {
        viewModel.sendIntent(FilesIntent.Load(modId))
    }

    FilesContent(
        state = state,
        onBack = { navigator.pop() },
        onRetry = { viewModel.sendIntent(FilesIntent.Load(modId)) },
        onIntent = viewModel::sendIntent,
    )
}

@Composable
private fun FilesContent(
    state: FilesState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onIntent: (FilesIntent) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.BackgroundPrimary)
            .systemBarsPadding(),
    ) {
        when (val status = state.status) {
            ScreenUiState.Loading -> LoadingState()
            is ScreenUiState.Error -> ErrorBlock(
                message = status.message,
                onBack = onBack,
                onRetry = onRetry,
            )
            ScreenUiState.Success -> SuccessState(
                state = state,
                onBack = onBack,
                onIntent = onIntent,
            )
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
private fun ErrorBlock(
    message: String,
    onBack: () -> Unit,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = HorizontalPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ListErrorBlock(
            title = stringResource(R.string.error_title_pagination),
            message = message,
            onClickRetry = onRetry,
        )
        Spacer(Modifier.height(16.dp))
        AppButton(
            text = stringResource(R.string.files_back),
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
    state: FilesState,
    onBack: () -> Unit,
    onIntent: (FilesIntent) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = HorizontalPadding,
            end = HorizontalPadding,
            top = 20.dp,
            bottom = 32.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item(key = "header") {
            FilesHeader(
                modTitle = state.modTitle,
                fileCount = state.items.size,
                onBack = onBack,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
        items(items = state.items, key = { it.url }) { item ->
            FileRow(
                item = item,
                onStartDownload = { onIntent(FilesIntent.StartDownload(item.url)) },
                onCancelDownload = { onIntent(FilesIntent.CancelDownload(item.url)) },
                onInstall = { onIntent(FilesIntent.Install(item.url)) },
            )
        }
    }
}
