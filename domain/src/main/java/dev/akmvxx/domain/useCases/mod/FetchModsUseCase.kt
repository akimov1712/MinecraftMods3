package dev.akmvxx.domain.useCases.mod

import android.R.attr.category
import dev.akmvxx.domain.entity.mod.FetchModsEntity
import dev.akmvxx.domain.entity.mod.ModCategory
import dev.akmvxx.domain.entity.mod.ModSorted
import dev.akmvxx.domain.repository.ModRepository

class FetchModsUseCase(
    private val repository: ModRepository
) {

    suspend fun fetch(
        searchQuery: String,
        category: ModCategory?,
        sorted: ModSorted,
        offset: Int,
        limit: Int,
    ) = repository.fetchMods(FetchModsEntity(searchQuery, category, sorted, offset, limit))

}