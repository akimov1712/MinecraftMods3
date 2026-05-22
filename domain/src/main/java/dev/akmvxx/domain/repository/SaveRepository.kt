package dev.akmvxx.domain.repository

import dev.akmvxx.domain.entity.SaveFileState
import kotlinx.coroutines.flow.Flow

interface SaveRepository {

    fun saveFile(url: String, name: String): Flow<SaveFileState>

    suspend fun isFileSaved(name: String): Boolean

    suspend fun openFile(name: String): Boolean
}
