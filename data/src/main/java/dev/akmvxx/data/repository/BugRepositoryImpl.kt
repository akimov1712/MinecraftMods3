package dev.akmvxx.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.akmvxx.common.Result
import dev.akmvxx.common.errors.DataError
import dev.akmvxx.data.BuildConfig
import dev.akmvxx.data.exceptionWrapper
import dev.akmvxx.data.source.remote.bug.BugApi
import dev.akmvxx.data.source.remote.bug.toDto
import dev.akmvxx.domain.entity.bug.BugEntity
import dev.akmvxx.domain.repository.BugRepository
import javax.inject.Inject

internal class BugRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: BugApi
): BugRepository {

    override suspend fun reportBug(bug: BugEntity): Result<Unit, DataError> =
        context.exceptionWrapper {
            val response = api.reportBug(appId = BuildConfig.APP_ID, bug = bug.toDto())
            val result = response.body()
            if (response.isSuccessful && result != null){
                Result.Success(Unit)
            } else {
                Result.Error( DataError.Network.SERVER_ERROR)
            }
        }


}