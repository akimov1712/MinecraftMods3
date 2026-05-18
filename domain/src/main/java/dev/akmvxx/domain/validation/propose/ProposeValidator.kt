package dev.akmvxx.domain.validation.propose

import dev.akmvxx.common.Result
import dev.akmvxx.common.Validator
import dev.akmvxx.common.errors.ValidatorError
import dev.akmvxx.common.isEmailValid
import dev.akmvxx.domain.entity.bug.BugEntity
import dev.akmvxx.domain.entity.propose.ProposeEntity
import dev.akmvxx.domain.validation.bug.BugValidatorError

class ProposeValidator: Validator<ProposeEntity> {

    override fun validate(data: ProposeEntity): Result<Unit, ProposeValidatorError> {
        val error =  when{
            data.email.isBlank() -> ProposeValidatorError.EMAIL_EMPTY
            data.message.isBlank() -> ProposeValidatorError.MESSAGE_EMPTY
            !data.email.isEmailValid() -> ProposeValidatorError.EMAIL_NOT_VALID
            data.message.length < MIN_MESSAGE_LENGTH -> ProposeValidatorError.MESSAGE_IS_SHORT
            else -> null
        }
        return error?.let { Result.Error(error) } ?: run { Result.Success(Unit) }
    }

    companion object{

        private const val MIN_MESSAGE_LENGTH = 40

    }

}

enum class ProposeValidatorError: ValidatorError {

    EMAIL_EMPTY,
    MESSAGE_EMPTY,
    EMAIL_NOT_VALID,
    MESSAGE_IS_SHORT

}