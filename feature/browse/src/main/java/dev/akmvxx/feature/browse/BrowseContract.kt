package dev.akmvxx.feature.browse

import androidx.compose.foundation.lazy.LazyListState
import dev.akmvxx.domain.entity.mod.ModEntity
import dev.akmvxx.ui.entity.ModCategoryUi
import dev.akmvxx.ui.entity.ModSortedUi
import dev.akmvxx.ui.entity.ScreenUiState

data class BrowseState(
    val mods: List<ModEntity> = emptyList(),
    val modsListEnd: Boolean = false,
    val modsListLazyState: LazyListState = LazyListState(),
    val searchQuery: String = "",
    val categories: List<ModCategoryUi> = ModCategoryUi.entries,
    val categoryIndexSelected: Int = 0,
    val sorted: List<ModSortedUi> = ModSortedUi.entries,
    val sortedIndexSelected: Int = 0,
    val fetchModsStateUi: ScreenUiState = ScreenUiState.Idle,
)

sealed interface BrowseIntent{

    data class ChangeSearchQuery(val value: String): BrowseIntent
    data class ChangeCategorySelected(val index: Int): BrowseIntent
    data class ChangeSortedSelected(val index: Int): BrowseIntent
    data object FetchMods: BrowseIntent
    data object RefreshModsList: BrowseIntent
    data object HandleChangeState: BrowseIntent

}

sealed interface BrowseEvent
