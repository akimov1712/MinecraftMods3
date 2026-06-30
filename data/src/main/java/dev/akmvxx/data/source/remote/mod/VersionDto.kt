package dev.akmvxx.data.source.remote.mod

import com.google.gson.annotations.SerializedName

internal data class VersionDto(
    @SerializedName("version") val name: String
)
