package dev.akmvxx.domain.repository

import dev.akmvxx.common.errors.DataError
import dev.akmvxx.common.Result
import dev.akmvxx.domain.entity.bug.BugEntity

interface BugRepository {

    suspend fun reportBug(bug: BugEntity): Result<Unit, DataError>

}
