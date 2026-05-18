package dev.akmvxx.feature.browse

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.akmvxx.android.MVI
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BrowseViewModel @Inject constructor() :
    MVI<BrowseIntent, BrowseState, BrowseEvent>(BrowseState()) {

    private fun changeSearchQuery(value: String) = _state.update { it.copy(searchQuery = value) }
    private fun changeCategorySelected(index: Int) = _state.update { it.copy(categoryIndexSelected = index) }
    private fun changeSortedSelected(index: Int) = _state.update { it.copy(sortedIndexSelected = index) }

    override suspend fun handleIntent(intent: BrowseIntent) {
        when (intent) {
            is BrowseIntent.ChangeSearchQuery -> changeSearchQuery(intent.value)
            is BrowseIntent.ChangeCategorySelected -> changeCategorySelected(intent.index)
            is BrowseIntent.ChangeSortedSelected -> changeSortedSelected(intent.index)
        }
    }
}
