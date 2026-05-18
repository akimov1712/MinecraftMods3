package dev.akmvxx.feature.browse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R
import dev.akmvxx.ui.components.AppDropdown
import dev.akmvxx.ui.components.AppTabRow
import dev.akmvxx.ui.components.AppTextField

@Composable
fun BrowseScreen(
    viewModel: BrowseViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
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
        Spacer(Modifier.height(16.dp))
        AppTabRow(
            items = state.categories.map { stringResource(it.titleRes) },
            selectedIndex = state.categoryIndexSelected,
            onTabSelected = { viewModel.sendIntent(BrowseIntent.ChangeCategorySelected(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(20.dp))
        AppDropdown(
            items = state.sorted.map{ stringResource(it.titleRes) },
            selectedIndex = state.sortedIndexSelected,
            onSelected = { viewModel.sendIntent(BrowseIntent.ChangeSortedSelected(it)) },
            modifier = Modifier.align(Alignment.End).padding(horizontal = 16.dp),
        )
    }
}

@Preview
@Composable
private fun BrowsePreview() {
    BrowseScreen()
}
