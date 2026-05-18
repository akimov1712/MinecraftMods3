package dev.akmvxx.common

import dev.akmvxx.common.errors.ValidatorError

interface Validator<T> {

    fun validate(data: T): Result<Unit, ValidatorError>

}