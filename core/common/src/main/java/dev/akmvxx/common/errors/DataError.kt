package dev.akmvxx.common.errors

sealed interface DataError: Error{

    enum class Network: DataError{

        REQUEST_TIMEOUT,
        SERIALIZATION,
        SERVER_ERROR,
        NO_INTERNET,
        UNKNOWN,

        INVALID_DATA,
        BAD_REQUEST,
        NOT_FOUND,
    }

}
