package dev.akmvxx.feature.browse

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.akmvxx.ads.NativeAds
import dev.akmvxx.feature.browse.ui.BrowseHeader
import dev.akmvxx.feature.browse.ui.BrowseSearchBar
import dev.akmvxx.feature.browse.ui.CategoryChips
import dev.akmvxx.navigation.RootNavKey
import dev.akmvxx.navigation.rootNavigator
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.components.ModsList

@Composable
fun BrowseScreen(
    viewModel: BrowseViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navigator = rootNavigator()
    LaunchedEffect(Unit) { viewModel.sendIntent(BrowseIntent.HandleChangeState) }

    val filterVisible by remember(state.modsListLazyState) {
        derivedStateOf { state.modsListLazyState.firstVisibleItemIndex < 1 }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.BackgroundPrimary)
            .systemBarsPadding()
            .padding(top = 16.dp),
    ) {
        BrowseHeader()
        Spacer(Modifier.height(18.dp))
        BrowseSearchBar(
            query = state.searchQuery,
            onQueryChange = { viewModel.sendIntent(BrowseIntent.ChangeSearchQuery(it)) },
            sortItems = state.sorted,
            sortedIndex = state.sortedIndexSelected,
            onSortSelected = { viewModel.sendIntent(BrowseIntent.ChangeSortedSelected(it)) },
        )

        val filterHeight = if (filterVisible) Modifier.wrapContentHeight() else Modifier.height(0.dp)
        CategoryChips(
            categories = state.categories,
            selectedIndex = state.categoryIndexSelected,
            onSelected = { viewModel.sendIntent(BrowseIntent.ChangeCategorySelected(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .then(filterHeight)
                .padding(top = 18.dp),
        )

        Box(Modifier.fillMaxWidth().weight(1f)) {
            ModsList(
                modifier = Modifier.fillMaxSize(),
                state = state.modsListLazyState,
                mods = state.mods,
                status = state.fetchModsStateUi,
                isEndList = state.modsListEnd,
                onRefresh = { viewModel.sendIntent(BrowseIntent.RefreshModsList) },
                onLoadMore = { viewModel.sendIntent(BrowseIntent.FetchMods) },
                onItemClick = { mod -> navigator.push(RootNavKey.ModDetail(mod.id)) },
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 110.dp),
                adInterval = state.nativeAdInterval,
                adContent = { slotKey ->
                    NativeAds.Show(
                        slot = NativeAds.Slot.Inline,
                        slotKey = slotKey,
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
            )
        }
    }
}

@Preview
@Composable
private fun BrowsePreview() {
    BrowseScreen()
}
