package dev.akmvxx.data.source.remote.mod

import com.google.gson.annotations.SerializedName
import dev.akmvxx.domain.entity.mod.ModCategory
import dev.akmvxx.domain.entity.mod.ModEntity

internal data class ModDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val aboutModMessage: String,
    @SerializedName("image") val imageUrl: String,
    @SerializedName("category") val category: ModCategory,
    @SerializedName("descriptionImages") val gallery: List<String>,
    @SerializedName("files") val downloadableFiles: List<String>,
    @SerializedName("versions") val supportVersions: List<VersionDto>,
){

    fun toEntity(isFavorite: Boolean) = ModEntity(
        id = id,
        title = title,
        aboutModMessage = aboutModMessage,
        imageUrl = imageUrl,
        category = category,
        gallery = gallery,
        downloadableFiles = downloadableFiles,
        supportVersions = supportVersions.map { it.name },
        isFavorite = isFavorite
    )

}
