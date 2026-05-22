package dev.akmvxx.domain.repository

import dev.akmvxx.domain.entity.SaveFileState
import kotlinx.coroutines.flow.Flow

interface SaveRepository {

    fun saveFile(url: String, name: String): Flow<SaveFileState>

    /**
     * Returns `true` if a file with [name] has already been saved by the app
     * into its downloads location.
     */
    suspend fun isFileSaved(name: String): Boolean

    /**
     * Launches a system viewer for a previously saved file with [name].
     * Returns `true` if the intent was successfully fired, `false` if the
     * file isn't present or no app can open it.
     */
    suspend fun openFile(name: String): Boolean
}
