package dev.akmvxx.feature.favorite

import android.content.Context
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.akmvxx.android.MVI
import dev.akmvxx.android.SnackbarManager
import dev.akmvxx.common.errors.DataError
import dev.akmvxx.common.onError
import dev.akmvxx.common.onSuccess
import dev.akmvxx.domain.useCases.favorite.FetchFavoriteModsUseCase
import dev.akmvxx.domain.useCases.favorite.GetCountFavoriteUseCase
import dev.akmvxx.ui.R
import dev.akmvxx.ui.entity.ScreenUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fetchFavoriteModsUseCase: FetchFavoriteModsUseCase,
    private val getCountFavoriteUseCase: GetCountFavoriteUseCase,
    private val snackbarManager: SnackbarManager,
) : MVI<FavoriteIntent, FavoriteState, FavoriteEvent>(FavoriteState()) {

    private var fetchJob: Job? = null

    init {
        refreshList()
    }

    private fun refreshList() {
        fetchJob?.cancel()

        _state.update {
            it.copy(
                mods = emptyList(),
                modsListEnd = false,
                fetchModsStateUi = ScreenUiState.Loading,
            )
        }

        loadCount()
        fetchMods()
    }

    private fun loadCount() {
        viewModelScope.launch {
            val count = getCountFavoriteUseCase.getCount()
            _state.update { it.copy(totalCount = count) }
        }
    }

    private fun fetchMods() = with(_state.value) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _state.update { it.copy(fetchModsStateUi = ScreenUiState.Loading) }
            val result = fetchFavoriteModsUseCase.fetch(
                offset = mods.size,
                limit = LIMIT_FETCH_MODS,
            )
            result.onSuccess { fetched ->
                _state.update {
                    it.copy(
                        mods = this@with.mods + fetched,
                        modsListEnd = fetched.isEmpty(),
                        fetchModsStateUi = ScreenUiState.Success,
                    )
                }
            }.onError { error, _ ->
                val messageRes = networkMessage(error)
                val message = context.getString(messageRes)
                snackbarManager.showMessage(message)
                _state.update { it.copy(fetchModsStateUi = ScreenUiState.Error(message)) }
            }
        }
    }

    private fun networkMessage(error: DataError): Int = when (error) {
        DataError.Network.REQUEST_TIMEOUT -> R.string.error_request_timeout
        DataError.Network.SERIALIZATION -> R.string.error_serialization
        DataError.Network.SERVER_ERROR -> R.string.error_server_error
        DataError.Network.NO_INTERNET -> R.string.error_no_internet
        DataError.Network.UNKNOWN -> R.string.error_unknown
        DataError.Network.INVALID_DATA -> R.string.error_invalid_data
        DataError.Network.BAD_REQUEST -> R.string.error_bad_request
        DataError.Network.NOT_FOUND -> R.string.error_not_found
    }

    override suspend fun handleIntent(intent: FavoriteIntent) {
        when (intent) {
            FavoriteIntent.FetchMods -> fetchMods()
            FavoriteIntent.RefreshList -> refreshList()
        }
    }

    companion object {
        private const val LIMIT_FETCH_MODS = 12
    }
}
