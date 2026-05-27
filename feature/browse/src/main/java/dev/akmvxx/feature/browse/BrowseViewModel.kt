package dev.akmvxx.feature.browse

import android.content.Context
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.akmvxx.android.MVI
import dev.akmvxx.android.SnackbarManager
import dev.akmvxx.common.errors.DataError
import dev.akmvxx.common.onError
import dev.akmvxx.common.onSuccess
import dev.akmvxx.common.Result
import dev.akmvxx.domain.useCases.mod.FetchModsUseCase
import dev.akmvxx.domain.useCases.settings.GetSettingsUseCase
import dev.akmvxx.ui.entity.ScreenUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowseViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fetchModsUseCase: FetchModsUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val snackbarManager: SnackbarManager
) : MVI<BrowseIntent, BrowseState, BrowseEvent>(BrowseState()) {

    private var fetchModsJob: Job? = null

    init {
        loadAdInterval()
    }

    private fun loadAdInterval() {
        viewModelScope.launch {
            val interval = when (val result = getSettingsUseCase.fetch()) {
                is Result.Success -> result.data.intervalNative
                is Result.Error -> result.data?.intervalNative ?: 0
            }
            _state.update { it.copy(nativeAdInterval = interval) }
        }
    }

    private fun changeSearchQuery(value: String) = _state.update { it.copy(searchQuery = value) }
    private fun changeCategorySelected(index: Int) =
        _state.update { it.copy(categoryIndexSelected = index) }

    private fun changeSortedSelected(index: Int) =
        _state.update { it.copy(sortedIndexSelected = index) }

    private fun observeModsChanges() {
        viewModelScope.launch {
            combine(
                state.map { it.searchQuery }.distinctUntilChanged(),
                state.map { it.sortedIndexSelected }.distinctUntilChanged(),
                state.map { it.categoryIndexSelected }.distinctUntilChanged()
            ) { search, type, filters ->
                Triple(search, type, filters)
            }.collect {
                refreshModsList()
            }
        }
    }

    private fun refreshModsList() {
        fetchModsJob?.cancel()

        _state.update {
            it.copy(
                mods = emptyList(),
                modsListEnd = false,
                fetchModsStateUi = ScreenUiState.Loading
            )
        }

        fetchMods()
    }

    private fun fetchMods() = with(_state.value) {
        fetchModsJob?.cancel()
        fetchModsJob = viewModelScope.launch {
            _state.update { it.copy(fetchModsStateUi = ScreenUiState.Loading) }
            val mods = fetchModsUseCase.fetch(
                searchQuery = searchQuery,
                category = categories[categoryIndexSelected].toModCategory(),
                sorted = sorted[sortedIndexSelected].toModSorted(),
                offset = mods.size,
                limit = LIMIT_FETCH_MODS
            )
            mods.onSuccess { result ->
                _state.update {
                    it.copy(
                        mods = this@with.mods + result,
                        modsListEnd = result.isEmpty(),
                        fetchModsStateUi = ScreenUiState.Success
                    )
                }
            }.onError { error, _ ->
                val messageRes = when (error) {
                    DataError.Network.REQUEST_TIMEOUT -> dev.akmvxx.ui.R.string.error_request_timeout
                    DataError.Network.SERIALIZATION -> dev.akmvxx.ui.R.string.error_serialization
                    DataError.Network.SERVER_ERROR -> dev.akmvxx.ui.R.string.error_server_error
                    DataError.Network.NO_INTERNET -> dev.akmvxx.ui.R.string.error_no_internet
                    DataError.Network.UNKNOWN -> dev.akmvxx.ui.R.string.error_unknown
                    DataError.Network.INVALID_DATA -> dev.akmvxx.ui.R.string.error_invalid_data
                    DataError.Network.BAD_REQUEST -> dev.akmvxx.ui.R.string.error_bad_request
                    DataError.Network.NOT_FOUND -> dev.akmvxx.ui.R.string.error_not_found
                }
                val message = context.getString(messageRes)
                snackbarManager.showMessage(message)
                _state.update { it.copy(fetchModsStateUi = ScreenUiState.Error(message)) }
            }
        }
    }

    override suspend fun handleIntent(intent: BrowseIntent) {
        when (intent) {
            is BrowseIntent.ChangeSearchQuery -> changeSearchQuery(intent.value)
            is BrowseIntent.ChangeCategorySelected -> changeCategorySelected(intent.index)
            is BrowseIntent.ChangeSortedSelected -> changeSortedSelected(intent.index)
            BrowseIntent.FetchMods -> fetchMods()
            BrowseIntent.HandleChangeState -> observeModsChanges()
            BrowseIntent.RefreshModsList -> refreshModsList()
        }
    }

    companion object {
        private const val LIMIT_FETCH_MODS = 6
    }
}
