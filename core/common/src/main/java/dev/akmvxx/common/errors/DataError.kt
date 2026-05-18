package dev.akmvxx.common.errors

sealed interface DataError: Error{

    enum class Network: DataError{
        // Default
        REQUEST_TIMEOUT,
        SERIALIZATION,
        SERVER_ERROR,
        NO_INTERNET,
        UNKNOWN,

        // Custom
        INVALID_DATA,
        BAD_REQUEST,
        NOT_FOUND,
    }

}