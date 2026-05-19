package dev.akmvxx.feature.help

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.akmvxx.android.MVI
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor() :
    MVI<HelpIntent, HelpState, HelpEvent>(HelpState()) {

    private fun changeSearchQuery(value: String) = _state.update {
        it.copy(searchQuery = value)
    }

    private fun toggleExpand(id: String) = _state.update {
        it.copy(expandedItemId = if (it.expandedItemId == id) null else id)
    }

    override suspend fun handleIntent(intent: HelpIntent) {
        when (intent) {
            is HelpIntent.ChangeSearchQuery -> changeSearchQuery(intent.value)
            is HelpIntent.ToggleExpand -> toggleExpand(intent.id)
        }
    }
}
