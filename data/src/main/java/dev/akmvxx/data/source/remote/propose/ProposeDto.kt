package dev.akmvxx.data.source.remote.propose

import com.google.gson.annotations.SerializedName
import dev.akmvxx.domain.entity.propose.ProposeEntity

internal data class ProposeDto(
    @SerializedName("email") val email: String,
    @SerializedName("description") val message: String,
)

internal fun ProposeEntity.toDto() = ProposeDto(email = email, message = message,)
