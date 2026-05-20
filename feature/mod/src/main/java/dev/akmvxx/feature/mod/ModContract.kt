package dev.akmvxx.feature.mod

import dev.akmvxx.domain.entity.mod.ModEntity
import dev.akmvxx.ui.entity.ScreenUiState

data class ModState(
    val mod: ModEntity? = null,
    val status: ScreenUiState = ScreenUiState.Loading,
)

sealed interface ModIntent {
    data class LoadMod(val modId: Int) : ModIntent
    data object ToggleFavorite : ModIntent
    data object Download : ModIntent
}

sealed interface ModEvent
