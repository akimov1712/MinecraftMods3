package dev.akmvxx.domain.repository

import dev.akmvxx.domain.entity.SaveFileState
import kotlinx.coroutines.flow.Flow

interface SaveRepository {

    fun saveFile(url: String, name: String): Flow<SaveFileState>
}
