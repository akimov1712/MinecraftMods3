package dev.akmvxx.feature.browse

import dev.akmvxx.domain.entity.mod.ModCategoryUi
import dev.akmvxx.domain.entity.mod.ModSortedUi

data class BrowseState(
    val searchQuery: String = "",
    val categories: List<ModCategoryUi> = ModCategoryUi.entries,
    val categoryIndexSelected: Int = 0,
    val sorted: List<ModSortedUi> = ModSortedUi.entries,
    val sortedIndexSelected: Int = 0
)

sealed interface BrowseIntent{

    data class ChangeSearchQuery(val value: String): BrowseIntent
    data class ChangeCategorySelected(val index: Int): BrowseIntent
    data class ChangeSortedSelected(val index: Int): BrowseIntent

}

sealed interface BrowseEvent