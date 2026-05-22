package dev.akmvxx.domain.useCases.save

import dev.akmvxx.domain.repository.SaveRepository

class OpenSavedFileUseCase(
    private val repository: SaveRepository,
) {

    suspend fun open(name: String): Boolean = repository.openFile(name)
}
