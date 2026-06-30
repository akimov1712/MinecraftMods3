package dev.akmvxx.ui.entity

sealed interface ScreenUiState {

    object Idle: ScreenUiState
    object Loading: ScreenUiState
    object Success: ScreenUiState
    data class Error(val message: String): ScreenUiState

    val isLoading: Boolean
        get() = this == Loading

    val isError: Boolean
        get() = this is Error

    val isSuccess: Boolean
        get() = this == Success
}
