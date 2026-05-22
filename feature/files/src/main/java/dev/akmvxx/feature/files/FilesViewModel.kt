package dev.akmvxx.feature.files

import android.content.Context
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.akmvxx.android.MVI
import dev.akmvxx.android.SnackbarManager
import dev.akmvxx.common.errors.DataError
import dev.akmvxx.common.extractFileNameOrFallback
import dev.akmvxx.common.onError
import dev.akmvxx.common.onSuccess
import dev.akmvxx.domain.entity.SaveFileState
import dev.akmvxx.domain.useCases.mod.FetchFileSizeUseCase
import dev.akmvxx.domain.useCases.mod.FetchModUseCase
import dev.akmvxx.domain.useCases.save.IsFileSavedUseCase
import dev.akmvxx.domain.useCases.save.OpenSavedFileUseCase
import dev.akmvxx.domain.useCases.save.SaveFileUseCase
import dev.akmvxx.ui.R
import dev.akmvxx.ui.entity.ScreenUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fetchModUseCase: FetchModUseCase,
    private val fetchFileSizeUseCase: FetchFileSizeUseCase,
    private val saveFileUseCase: SaveFileUseCase,
    private val isFileSavedUseCase: IsFileSavedUseCase,
    private val openSavedFileUseCase: OpenSavedFileUseCase,
    private val snackbarManager: SnackbarManager,
) : MVI<FilesIntent, FilesState, FilesEvent>(FilesState()) {

    private val downloadJobs = mutableMapOf<String, Job>()
    private val stallWatchers = mutableMapOf<String, Job>()

    private fun loadFiles(modId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(status = ScreenUiState.Loading) }
            fetchModUseCase.fetch(modId)
                .onSuccess { mod ->
                    val extension = mod.category.getExtensionFile()
                    val items = mod.downloadableFiles.mapIndexed { index, url ->
                        FileItemUi(
                            url = url,
                            name = url.extractFileNameOrFallback(extension)
                                ?: defaultName(index, extension),
                            sizeBytes = null,
                            status = FileStatus.Idle,
                        )
                    }
                    _state.update {
                        it.copy(
                            modTitle = mod.title,
                            items = items,
                            status = ScreenUiState.Success,
                        )
                    }
                    items.forEach { item ->
                        resolveExistingStatus(item.url, item.name)
                        loadFileSize(item.url)
                    }
                }
                .onError { error, _ ->
                    val message = context.getString(networkMessage(error))
                    snackbarManager.showMessage(message)
                    _state.update { it.copy(status = ScreenUiState.Error(message)) }
                }
        }
    }

    private fun resolveExistingStatus(url: String, name: String) {
        viewModelScope.launch {
            if (isFileSavedUseCase.isSaved(name)) {
                updateItemStatus(url) { FileStatus.Downloaded }
            }
        }
    }

    private fun loadFileSize(url: String) {
        viewModelScope.launch {
            fetchFileSizeUseCase.fetch(url).onSuccess { size ->
                updateItem(url) { it.copy(sizeBytes = size) }
            }
        }
    }

    private fun startDownload(url: String) {
        val item = findItem(url) ?: return
        if (item.status is FileStatus.Downloading) return
        if (item.status is FileStatus.Downloaded) {
            install(url)
            return
        }

        updateItem(url) {
            it.copy(
                status = FileStatus.Downloading(
                    downloadedBytes = 0L,
                    totalBytes = it.sizeBytes ?: 0L,
                ),
                stalled = false,
            )
        }
        scheduleStallWatcher(url)

        downloadJobs[url] = viewModelScope.launch {
            saveFileUseCase.save(url, item.name).collect { saveState ->
                when (saveState) {
                    is SaveFileState.Saving -> handleSaving(url, saveState)
                    SaveFileState.Success -> {
                        cancelStallWatcher(url)
                        updateItem(url) {
                            it.copy(status = FileStatus.Downloaded, stalled = false)
                        }
                    }
                    SaveFileState.Error -> {
                        cancelStallWatcher(url)
                        updateItem(url) {
                            it.copy(status = FileStatus.Idle, stalled = false)
                        }
                        snackbarManager.showMessage(
                            context.getString(R.string.files_download_failed),
                        )
                    }
                }
            }
            downloadJobs.remove(url)
        }
    }

    private fun handleSaving(url: String, saving: SaveFileState.Saving) {
        val current = findItem(url) ?: return
        val previousBytes = (current.status as? FileStatus.Downloading)?.downloadedBytes ?: 0L
        val madeProgress = saving.downloadedBytes > previousBytes

        updateItem(url) {
            it.copy(
                status = FileStatus.Downloading(
                    downloadedBytes = saving.downloadedBytes,
                    totalBytes = saving.totalBytes,
                ),
                stalled = if (madeProgress) false else it.stalled,
            )
        }
        scheduleStallWatcher(url)
    }

    private fun cancelDownload(url: String) {
        downloadJobs.remove(url)?.cancel()
        cancelStallWatcher(url)
        updateItem(url) { it.copy(status = FileStatus.Idle, stalled = false) }
    }

    private fun install(url: String) {
        val item = findItem(url) ?: return
        viewModelScope.launch {
            val opened = openSavedFileUseCase.open(item.name)
            if (!opened) {
                snackbarManager.showMessage(context.getString(R.string.files_open_failed))
            }
        }
    }

    private fun scheduleStallWatcher(url: String) {
        stallWatchers[url]?.cancel()
        stallWatchers[url] = viewModelScope.launch {
            delay(STALL_THRESHOLD_MS)
            updateItem(url) { current ->
                if (current.status is FileStatus.Downloading) {
                    current.copy(stalled = true)
                } else current
            }
        }
    }

    private fun cancelStallWatcher(url: String) {
        stallWatchers.remove(url)?.cancel()
    }

    private fun findItem(url: String): FileItemUi? =
        _state.value.items.firstOrNull { it.url == url }

    private inline fun updateItem(url: String, transform: (FileItemUi) -> FileItemUi) {
        _state.update { state ->
            state.copy(
                items = state.items.map { item ->
                    if (item.url == url) transform(item) else item
                },
            )
        }
    }

    private inline fun updateItemStatus(url: String, transform: (FileStatus) -> FileStatus) {
        updateItem(url) { it.copy(status = transform(it.status)) }
    }

    private fun defaultName(index: Int, extension: String): String =
        "file_${index + 1}${if (extension.startsWith('.')) extension else ".$extension"}"

    private fun networkMessage(error: DataError): Int = when (error) {
        DataError.Network.REQUEST_TIMEOUT -> R.string.error_request_timeout
        DataError.Network.SERIALIZATION -> R.string.error_serialization
        DataError.Network.SERVER_ERROR -> R.string.error_server_error
        DataError.Network.NO_INTERNET -> R.string.error_no_internet
        DataError.Network.UNKNOWN -> R.string.error_unknown
        DataError.Network.INVALID_DATA -> R.string.error_invalid_data
        DataError.Network.BAD_REQUEST -> R.string.error_bad_request
        DataError.Network.NOT_FOUND -> R.string.error_not_found
    }

    override fun onCleared() {
        super.onCleared()
        // viewModelScope.cancel() already propagates cancellation to all
        // child download jobs, which makes SaveRepositoryImpl drop the
        // partially written file via its CancellationException handler.
        // Maps are cleared just to keep state tidy.
        downloadJobs.clear()
        stallWatchers.clear()
    }

    override suspend fun handleIntent(intent: FilesIntent) {
        when (intent) {
            is FilesIntent.Load -> loadFiles(intent.modId)
            is FilesIntent.StartDownload -> startDownload(intent.url)
            is FilesIntent.CancelDownload -> cancelDownload(intent.url)
            is FilesIntent.Install -> install(intent.url)
        }
    }

    private companion object {
        const val STALL_THRESHOLD_MS = 5_000L
    }
}
