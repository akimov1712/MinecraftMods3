package dev.akmvxx.domain.useCases.favorite

import dev.akmvxx.domain.repository.FavoriteRepository

class FetchFavoriteModsUseCase(
    private val repository: FavoriteRepository
) {

    suspend fun fetch(offset: Int, limit: Int) = repository.fetchFavoriteMods(offset, limit)

}
