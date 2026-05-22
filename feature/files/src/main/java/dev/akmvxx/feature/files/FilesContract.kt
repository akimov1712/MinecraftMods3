package dev.akmvxx.feature.files

import dev.akmvxx.ui.entity.ScreenUiState

data class FilesState(
    val modTitle: String = "",
    val items: List<FileItemUi> = emptyList(),
    val status: ScreenUiState = ScreenUiState.Loading,
) {
    val showVpnHint: Boolean get() = items.any { it.stalled }
}

data class FileItemUi(
    val url: String,
    val name: String,
    val sizeBytes: Long?,
    val status: FileStatus,
    val stalled: Boolean = false,
)

sealed interface FileStatus {
    data object Idle : FileStatus

    data class Downloading(
        val downloadedBytes: Long,
        val totalBytes: Long,
    ) : FileStatus {
        val hasKnownTotal: Boolean get() = totalBytes > 0L
        val progressFraction: Float
            get() = if (hasKnownTotal) {
                (downloadedBytes.toFloat() / totalBytes).coerceIn(0f, 1f)
            } else 0f
        val progressPercent: Int get() = (progressFraction * 100f).toInt()
    }

    data object Downloaded : FileStatus
}

sealed interface FilesIntent {
    data class Load(val modId: Int) : FilesIntent
    data class StartDownload(val url: String) : FilesIntent
    data class CancelDownload(val url: String) : FilesIntent
    data class Install(val url: String) : FilesIntent
}

sealed interface FilesEvent
