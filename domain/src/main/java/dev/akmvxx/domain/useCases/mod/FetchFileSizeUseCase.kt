package dev.akmvxx.domain.useCases.mod

import dev.akmvxx.domain.repository.ModRepository

class FetchFileSizeUseCase(
    private val repository: ModRepository
) {

    suspend fun fetch(url: String) = repository.fetchFileSize(url)

}
