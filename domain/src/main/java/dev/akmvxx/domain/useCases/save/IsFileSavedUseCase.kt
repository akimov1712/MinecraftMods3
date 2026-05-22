package dev.akmvxx.domain.useCases.save

import dev.akmvxx.domain.repository.SaveRepository

class IsFileSavedUseCase(
    private val repository: SaveRepository,
) {

    suspend fun isSaved(name: String): Boolean = repository.isFileSaved(name)
}
