package dev.akmvxx.domain.useCases.favorite

import dev.akmvxx.domain.repository.FavoriteRepository

class GetCountFavoriteUseCase(
    private val repository: FavoriteRepository
) {

    suspend fun getCount() = repository.getCountFavorites()

}