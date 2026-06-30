package dev.akmvxx.domain.useCases.mod

import dev.akmvxx.domain.repository.ModRepository

class FetchModUseCase(
    private val repository: ModRepository
) {

    suspend fun fetch(id: Int) = repository.fetchMod(id)

}
