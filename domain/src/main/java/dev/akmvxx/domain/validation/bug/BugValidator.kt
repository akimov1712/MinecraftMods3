package dev.akmvxx.domain.validation.bug

import dev.akmvxx.common.Result
import dev.akmvxx.common.Validator
import dev.akmvxx.common.errors.ValidatorError
import dev.akmvxx.common.isEmailValid
import dev.akmvxx.domain.entity.bug.BugEntity

class BugValidator: Validator<BugEntity> {

    override fun validate(data: BugEntity): Result<Unit, BugValidatorError> {
        val error =  when{
            data.email.isBlank() -> BugValidatorError.EMAIL_EMPTY
            data.message.isBlank() -> BugValidatorError.MESSAGE_EMPTY
            !data.email.isEmailValid() -> BugValidatorError.EMAIL_NOT_VALID
            data.message.length < MIN_MESSAGE_LENGTH -> BugValidatorError.MESSAGE_IS_SHORT
            else -> null
        }
        return error?.let { Result.Error(error) } ?: run { Result.Success(Unit) }
    }

    companion object{

        private const val MIN_MESSAGE_LENGTH = 20

    }

}

enum class BugValidatorError: ValidatorError {

    EMAIL_EMPTY,
    MESSAGE_EMPTY,
    EMAIL_NOT_VALID,
    MESSAGE_IS_SHORT

}