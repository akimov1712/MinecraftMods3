package dev.akmvxx.data.source.remote.mod

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ModsResponse(
    @SerialName("count") val size: Int,
    @SerialName("mods") val mods: List<ModDto>
)
