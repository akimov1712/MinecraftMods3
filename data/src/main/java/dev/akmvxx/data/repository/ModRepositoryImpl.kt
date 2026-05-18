package dev.akmvxx.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.akmvxx.android.deviceLanguage
import dev.akmvxx.common.Result
import dev.akmvxx.common.errors.DataError
import dev.akmvxx.data.BuildConfig
import dev.akmvxx.data.exceptionWrapper
import dev.akmvxx.data.source.local.database.FavoriteDao
import dev.akmvxx.data.source.remote.mod.ModApi
import dev.akmvxx.domain.entity.mod.FetchModsEntity
import dev.akmvxx.domain.entity.mod.ModEntity
import dev.akmvxx.domain.repository.ModRepository
import javax.inject.Inject

internal class ModRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dao: FavoriteDao,
    private val api: ModApi
): ModRepository {

    override suspend fun fetchMods(data: FetchModsEntity): Result<List<ModEntity>, DataError> =
        context.exceptionWrapper {
            val favoriteIds = dao.selectFavoriteIds()
            val response = api.fetchMods(
                appId = BuildConfig.APP_ID,
                language = deviceLanguage(),
                searchQuery = data.searchQuery,
                category = data.category,
                keySorted = data.sorted.toKeySorted(),
                offset = data.offset,
                take = data.limit,
            )
            val result = response.body()
            if (response.isSuccessful && result != null){
                Result.Success(
                    result.map { mod -> mod.toEntity(isFavorite = favoriteIds.contains(mod.id)) }
                )
            } else {
                Result.Error(DataError.Network.SERVER_ERROR)
            }
        }

    override suspend fun fetchMod(id: Int): Result<ModEntity, DataError> =
        context.exceptionWrapper {
            val favoriteIds = dao.selectFavoriteIds()
            val response = api.fetchMod(modId = id, language = deviceLanguage())
            val result = response.body()
            if (response.isSuccessful && result != null){
                Result.Success(result.toEntity(isFavorite = favoriteIds.contains(id)))
            } else {
                Result.Error(DataError.Network.SERVER_ERROR)
            }
        }

}