package dev.akmvxx.domain.repository

import dev.akmvxx.common.errors.DataError
import dev.akmvxx.common.Result
import dev.akmvxx.domain.entity.mod.FetchModsEntity
import dev.akmvxx.domain.entity.mod.ModEntity

interface ModRepository {

    suspend fun fetchMods(data: FetchModsEntity): Result<List<ModEntity>, DataError>
    suspend fun fetchMod(id: Int): Result<ModEntity, DataError>
    suspend fun fetchFileSize(url: String): Result<Long, DataError>

}
