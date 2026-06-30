package dev.akmvxx.domain.repository

import dev.akmvxx.common.errors.DataError
import dev.akmvxx.common.Result
import dev.akmvxx.domain.entity.mod.ModEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    suspend fun getCountFavorites(): Int
    suspend fun fetchFavoriteMods(offset: Int, limit: Int): Result<List<ModEntity>, DataError>
    suspend fun changeStatusFavorite(modId: Int): Boolean

}
