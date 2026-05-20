package dev.akmvxx.feature.favorite

import androidx.compose.foundation.lazy.LazyListState
import dev.akmvxx.domain.entity.mod.ModEntity
import dev.akmvxx.ui.entity.ScreenUiState

data class FavoriteState(
    val mods: List<ModEntity> = emptyList(),
    val totalCount: Int = 0,
    val modsListLazyState: LazyListState = LazyListState(),
    val modsListEnd: Boolean = false,
    val fetchModsStateUi: ScreenUiState = ScreenUiState.Loading,
)

sealed interface FavoriteIntent {
    data object FetchMods : FavoriteIntent
    data object RefreshList : FavoriteIntent
}

sealed interface FavoriteEvent
