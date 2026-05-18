package dev.akmvxx.domain.entity.mod

data class FetchModsEntity(
    val searchQuery: String,
    val category: ModCategory?,
    val sorted: ModSorted,
    val offset: Int,
    val limit: Int,
)
