package dev.akmvxx.domain.useCases.propose

import dev.akmvxx.common.errors.DataError
import dev.akmvxx.common.onError
import dev.akmvxx.common.Result
import dev.akmvxx.common.errors.Error
import dev.akmvxx.common.onSuccess
import dev.akmvxx.domain.entity.bug.BugEntity
import dev.akmvxx.domain.entity.propose.ProposeEntity
import dev.akmvxx.domain.repository.BugRepository
import dev.akmvxx.domain.repository.ProposeRepository
import dev.akmvxx.domain.validation.bug.BugValidator
import dev.akmvxx.domain.validation.propose.ProposeValidator

class ProposeModUseCase(
    private val repository: ProposeRepository,
    private val validator: ProposeValidator,
){

    suspend fun report(email: String, message: String): Result<Unit, Error> {
        val propose = ProposeEntity(email, message)
        validator.validate(propose)
            .onSuccess {
                return repository.proposeMod(propose)
            }.onError{ error, _ ->
                return Result.Error(error)
            }
        return Result.Success(Unit)
    }

}
