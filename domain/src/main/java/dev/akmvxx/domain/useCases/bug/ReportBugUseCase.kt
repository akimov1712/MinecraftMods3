package dev.akmvxx.domain.useCases.bug

import dev.akmvxx.common.errors.DataError
import dev.akmvxx.common.onError
import dev.akmvxx.common.Result
import dev.akmvxx.common.errors.Error
import dev.akmvxx.common.onSuccess
import dev.akmvxx.domain.entity.bug.BugEntity
import dev.akmvxx.domain.repository.BugRepository
import dev.akmvxx.domain.validation.bug.BugValidator

class ReportBugUseCase(
    private val repository: BugRepository,
    private val validator: BugValidator,
){

    suspend fun report(email: String, message: String): Result<Unit, Error> {
        val bug = BugEntity(email, message)
        validator.validate(bug)
            .onSuccess {
                return repository.reportBug(bug)
            }.onError{ error, _ ->
                return Result.Error(error)
            }
        return Result.Success(Unit)
    }

}