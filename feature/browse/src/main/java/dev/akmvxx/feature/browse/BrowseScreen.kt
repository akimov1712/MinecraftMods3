package dev.akmvxx.feature.browse

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.akmvxx.ads.NativeAds
import dev.akmvxx.navigation.RootNavKey
import dev.akmvxx.navigation.rootNavigator
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R
import dev.akmvxx.ui.components.AppDropdown
import dev.akmvxx.ui.components.AppTabRow
import dev.akmvxx.ui.components.AppTextField
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
            .padding(top = 20.dp),
    ) {
        AppTextField(
            value = state.searchQuery,
            onValueChange = { viewModel.sendIntent(BrowseIntent.ChangeSearchQuery(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder = stringResource(R.string.search_mods),
            leadingIcon = Icons.Filled.Search,
        )

        val height = if (filterVisible) Modifier.wrapContentHeight() else Modifier.height(0.dp)
        AppTabRow(
            modifier = Modifier.fillMaxWidth()
                .animateContentSize()
                .then(height)
                .padding(top = 16.dp),
            items = state.categories.map { stringResource(it.titleRes) },
            selectedIndex = state.categoryIndexSelected,
            onTabSelected = { viewModel.sendIntent(BrowseIntent.ChangeCategorySelected(it)) },
        )
        Spacer(Modifier.height(10.dp))
        Box(Modifier.fillMaxWidth().weight(1f)){
            val animateOffsetX by animateDpAsState(if (filterVisible) 0.dp else 180.dp)
            ModsList(
                modifier = Modifier.fillMaxSize(),
                state = state.modsListLazyState,
                mods = state.mods,
                status = state.fetchModsStateUi,
                isEndList = state.modsListEnd,
                onRefresh = { viewModel.sendIntent(BrowseIntent.RefreshModsList) },
                onLoadMore = { viewModel.sendIntent(BrowseIntent.FetchMods) },
                onItemClick = { mod -> navigator.push(RootNavKey.ModDetail(mod.id)) },
                adInterval = state.nativeAdInterval,
                adContent = { slotKey ->
                    NativeAds.Show(
                        slot = NativeAds.Slot.Inline,
                        slotKey = slotKey,
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
            )
            AppDropdown(
                items = state.sorted.map { stringResource(it.titleRes) },
                selectedIndex = state.sortedIndexSelected,
                onSelected = { viewModel.sendIntent(BrowseIntent.ChangeSortedSelected(it)) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(horizontal = 16.dp)
                    .padding(top = 10.dp)
                    .offset(x = animateOffsetX),
            )
        }
    }
}



@Preview
@Composable
private fun BrowsePreview() {
    BrowseScreen()
}
