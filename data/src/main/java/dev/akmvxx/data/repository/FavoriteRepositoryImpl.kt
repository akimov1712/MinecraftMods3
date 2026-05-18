package dev.akmvxx.data.repository

import android.content.Context
import android.util.Log.e
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.akmvxx.android.deviceLanguage
import dev.akmvxx.common.Result
import dev.akmvxx.common.errors.DataError
import dev.akmvxx.data.exceptionWrapper
import dev.akmvxx.data.source.local.database.FavoriteDao
import dev.akmvxx.data.source.local.database.FavoriteDbo
import dev.akmvxx.data.source.remote.mod.ModApi
import dev.akmvxx.domain.entity.mod.ModEntity
import dev.akmvxx.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class FavoriteRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dao: FavoriteDao,
    private val api: ModApi
): FavoriteRepository {

    override suspend fun getCountFavorites(): Int = dao.selectCountFavorites()

    override suspend fun fetchFavoriteMods(offset: Int, limit: Int): Result<List<ModEntity>, DataError> =
        context.exceptionWrapper {
            val favorites = dao.selectFavorites(limit, offset)
            val mods = favorites.map{
                val response = api.fetchMod(modId = it.modId, language = deviceLanguage())
                val result = response.body()
                if (response.isSuccessful && result != null){
                    result.toEntity(isFavorite = true)
                } else {
                    return@exceptionWrapper Result.Error(DataError.Network.SERVER_ERROR)
                }
            }
            Result.Success(mods)
        }

    override suspend fun changeStatusFavorite(modId: Int): Boolean {
        val old = dao.selectFavorite(modId)
        val new = old?.copy(status = !old.status) ?: FavoriteDbo(modId, true)
        dao.insertFavorite(new)
        return new.status
    }

}