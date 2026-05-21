package dev.akmvxx.domain.useCases.save

import dev.akmvxx.domain.entity.SaveFileState
import dev.akmvxx.domain.repository.SaveRepository
import kotlinx.coroutines.flow.Flow

class SaveFileUseCase(
    private val repository: SaveRepository,
) {

    fun save(url: String, name: String): Flow<SaveFileState> =
        repository.saveFile(url, name)
}
