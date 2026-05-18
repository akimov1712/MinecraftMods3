package dev.akmvxx.domain.entity.mod

data class ModEntity(
    val id: Int,
    val title: String,
    val aboutModMessage: String,
    val imageUrl: String,
    val category: ModCategory,
    val isFavorite: Boolean,
    val gallery: List<String>,
    val downloadableFiles: List<String>,
    val supportVersions: List<String>,
)

