package dev.akmvxx.domain.entity

sealed interface SaveFileState {

    data class Saving(
        val downloadedBytes: Long,
        val totalBytes: Long,
    ) : SaveFileState {

        val hasKnownTotal: Boolean
            get() = totalBytes > 0L

        val progressFraction: Float
            get() = if (hasKnownTotal) {
                (downloadedBytes.toFloat() / totalBytes).coerceIn(0f, 1f)
            } else 0f

        val progressPercent: Int
            get() = (progressFraction * 100f).toInt()
    }

    data object Success : SaveFileState

    data object Error : SaveFileState
}
