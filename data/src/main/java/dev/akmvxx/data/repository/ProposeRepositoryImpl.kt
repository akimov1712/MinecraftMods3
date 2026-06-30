package dev.akmvxx.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.akmvxx.common.Result
import dev.akmvxx.common.errors.DataError
import dev.akmvxx.data.BuildConfig
import dev.akmvxx.data.exceptionWrapper
import dev.akmvxx.data.source.remote.propose.ProposeApi
import dev.akmvxx.data.source.remote.propose.toDto
import dev.akmvxx.domain.entity.propose.ProposeEntity
import dev.akmvxx.domain.repository.ProposeRepository
import javax.inject.Inject

internal class ProposeRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: ProposeApi
): ProposeRepository {

    override suspend fun proposeMod(propose: ProposeEntity): Result<Unit, DataError> =
        context.exceptionWrapper {
            val response = api.proposeMod(appId = BuildConfig.APP_ID, propose = propose.toDto())
            val result = response.body()
            if (response.isSuccessful && result != null){
                Result.Success(Unit)
            } else {
                Result.Error( DataError.Network.SERVER_ERROR)
            }
        }

}
