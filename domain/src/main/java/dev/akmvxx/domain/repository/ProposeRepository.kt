package dev.akmvxx.domain.repository

import dev.akmvxx.common.errors.DataError
import dev.akmvxx.common.Result
import dev.akmvxx.domain.entity.propose.ProposeEntity

interface ProposeRepository {

    suspend fun proposeMod(propose: ProposeEntity): Result<Unit, DataError>

}