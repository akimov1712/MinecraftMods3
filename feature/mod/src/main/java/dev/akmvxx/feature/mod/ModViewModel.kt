package dev.akmvxx.feature.mod

import android.content.Context
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.akmvxx.android.MVI
import dev.akmvxx.android.SnackbarManager
import dev.akmvxx.common.errors.DataError
import dev.akmvxx.common.onError
import dev.akmvxx.common.onSuccess
import dev.akmvxx.domain.useCases.favorite.ChangeStatusFavoriteUseCase
import dev.akmvxx.domain.useCases.mod.FetchModUseCase
import dev.akmvxx.ui.R
import dev.akmvxx.ui.entity.ScreenUiState
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fetchModUseCase: FetchModUseCase,
    private val changeStatusFavoriteUseCase: ChangeStatusFavoriteUseCase,
    private val snackbarManager: SnackbarManager,
) : MVI<ModIntent, ModState, ModEvent>(ModState()) {

    private fun loadMod(modId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(status = ScreenUiState.Loading) }
            fetchModUseCase.fetch(modId)
                .onSuccess { mod ->
                    _state.update {
                        it.copy(mod = mod, status = ScreenUiState.Success)
                    }
                }
                .onError { error, _ ->
                    val messageRes = networkMessage(error)
                    val message = context.getString(messageRes)
                    snackbarManager.showMessage(message)
                    _state.update { it.copy(status = ScreenUiState.Error(message)) }
                }
        }
    }

    private fun toggleFavorite() {
        val current = _state.value.mod ?: return
        viewModelScope.launch {
            val newFavorite = changeStatusFavoriteUseCase.change(current.id)
            _state.update {
                it.copy(mod = current.copy(isFavorite = newFavorite))
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

    override suspend fun handleIntent(intent: ModIntent) {
        when (intent) {
            is ModIntent.LoadMod -> loadMod(intent.modId)
            ModIntent.ToggleFavorite -> toggleFavorite()
            ModIntent.Download -> Unit
        }
    }
}
