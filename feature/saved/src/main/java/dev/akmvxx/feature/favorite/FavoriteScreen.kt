package dev.akmvxx.feature.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.akmvxx.feature.favorite.ui.FavoriteHeader
import dev.akmvxx.navigation.RootNavKey
import dev.akmvxx.navigation.rootNavigator
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.components.ModsList

@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navigator = rootNavigator()
    FavoriteContent(
        state = state,
        onIntent = viewModel::sendIntent,
        onModClick = { mod -> navigator.push(RootNavKey.ModDetail(mod.id)) },
    )
}

@Composable
private fun FavoriteContent(
    state: FavoriteState,
    onIntent: (FavoriteIntent) -> Unit,
    onModClick: (dev.akmvxx.domain.entity.mod.ModEntity) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.BackgroundPrimary)
            .systemBarsPadding(),
    ) {
        FavoriteHeader(
            count = state.totalCount,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp),
        )

        ModsList(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = state.modsListLazyState,
            mods = state.mods,
            status = state.fetchModsStateUi,
            isEndList = state.modsListEnd,
            onRefresh = { onIntent(FavoriteIntent.RefreshList) },
            onLoadMore = { onIntent(FavoriteIntent.FetchMods) },
            onItemClick = onModClick,
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 100.dp,
            ),
        )
    }
}
