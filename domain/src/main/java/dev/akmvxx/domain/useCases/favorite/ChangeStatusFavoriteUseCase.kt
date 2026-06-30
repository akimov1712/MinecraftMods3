package dev.akmvxx.domain.useCases.favorite

import dev.akmvxx.domain.entity.favorite.FavoriteEntity
import dev.akmvxx.domain.repository.FavoriteRepository

class ChangeStatusFavoriteUseCase(
    private val repository: FavoriteRepository
) {

    suspend fun change(modId: Int) = repository.changeStatusFavorite(modId)

}
