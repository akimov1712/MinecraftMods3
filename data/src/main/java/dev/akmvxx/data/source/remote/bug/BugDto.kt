package dev.akmvxx.data.source.remote.bug

import com.google.gson.annotations.SerializedName
import dev.akmvxx.domain.entity.bug.BugEntity

internal data class BugDto(
    @SerializedName("text") val message: String,
    @SerializedName("email") val email: String,
)

internal fun BugEntity.toDto() = BugDto(
    message = message,
    email = email
)