package dev.akmvxx.data.repository

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.akmvxx.domain.entity.SaveFileState
import dev.akmvxx.domain.repository.SaveRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SaveRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val okHttpClient: OkHttpClient,
) : SaveRepository {

    override fun saveFile(url: String, name: String): Flow<SaveFileState> = flow {
        var target: SaveTarget? = null
        try {
            okHttpClient.newCall(Request.Builder().url(url).build()).execute().use { response ->
                val body = response.body
                if (!response.isSuccessful || body == null) {
                    emit(SaveFileState.Error)
                    return@use
                }
                val createdTarget = createTarget(name)
                if (createdTarget == null) {
                    emit(SaveFileState.Error)
                    return@use
                }
                target = createdTarget

                val totalBytes = body.contentLength()
                emit(SaveFileState.Saving(downloadedBytes = 0L, totalBytes = totalBytes))

                createdTarget.outputStream.use { output ->
                    body.byteStream().use { input ->
                        streamWithProgress(input, output, totalBytes) { state -> emit(state) }
                    }
                }
                createdTarget.finalize()
                emit(SaveFileState.Success)
            }
        } catch (e: CancellationException) {
            target?.delete()
            throw e
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Save failed for $name", e)
            target?.delete()
            emit(SaveFileState.Error)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun isFileSaved(name: String): Boolean = withContext(Dispatchers.IO) {
        savedFile(name).let { it.exists() && it.isFile }
    }

    override suspend fun openFile(name: String): Boolean = withContext(Dispatchers.IO) {
        val file = savedFile(name)
        if (!file.exists() || !file.isFile) return@withContext false

        runCatching {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file,
            )
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, MIME_TYPE)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }.onFailure {
            Log.e(LOG_TAG, "Failed to open $name", it)
        }.isSuccess
    }

    @Suppress("DEPRECATION")
    private fun savedFile(name: String): File {
        val dir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            SUBDIR,
        )
        return File(dir, name)
    }

    private suspend inline fun streamWithProgress(
        input: InputStream,
        output: OutputStream,
        totalBytes: Long,
        crossinline emit: suspend (SaveFileState) -> Unit,
    ) {
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var downloaded = 0L
        var lastEmittedBytes = 0L
        while (true) {
            currentCoroutineContext().ensureActive()
            val read = input.read(buffer)
            if (read <= 0) break
            output.write(buffer, 0, read)
            downloaded += read
            if (downloaded - lastEmittedBytes >= EMIT_THRESHOLD_BYTES) {
                lastEmittedBytes = downloaded
                emit(SaveFileState.Saving(downloadedBytes = downloaded, totalBytes = totalBytes))
            }
        }
        if (lastEmittedBytes != downloaded) {
            emit(SaveFileState.Saving(downloadedBytes = downloaded, totalBytes = totalBytes))
        }
    }

    private fun createTarget(name: String): SaveTarget? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            createMediaStoreTarget(name)
        } else {
            createLegacyTarget(name)
        }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun createMediaStoreTarget(name: String): SaveTarget? {
        val resolver = context.contentResolver
        val collection = MediaStore.Downloads.EXTERNAL_CONTENT_URI
        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, name)
            put(MediaStore.Downloads.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/$SUBDIR")
            put(MediaStore.Downloads.IS_PENDING, 1)
        }
        val uri = resolver.insert(collection, values) ?: return null
        val output = resolver.openOutputStream(uri) ?: run {
            resolver.delete(uri, null, null)
            return null
        }
        return SaveTarget.MediaStoreTarget(
            uri = uri,
            outputStream = output,
            onFinalize = {
                val finalize = ContentValues().apply {
                    put(MediaStore.Downloads.IS_PENDING, 0)
                }
                resolver.update(uri, finalize, null, null)
            },
            onDelete = { resolver.delete(uri, null, null) },
        )
    }

    @Suppress("DEPRECATION")
    private fun createLegacyTarget(name: String): SaveTarget? {
        val downloadsDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            SUBDIR,
        )
        if (!downloadsDir.exists() && !downloadsDir.mkdirs()) return null
        val file = File(downloadsDir, name)
        val output = runCatching { file.outputStream() }.getOrNull() ?: return null
        return SaveTarget.LegacyTarget(file = file, outputStream = output)
    }

    private sealed class SaveTarget {
        abstract val outputStream: OutputStream
        abstract fun finalize()
        abstract fun delete()

        class MediaStoreTarget(
            val uri: Uri,
            override val outputStream: OutputStream,
            private val onFinalize: () -> Unit,
            private val onDelete: () -> Unit,
        ) : SaveTarget() {
            override fun finalize() = onFinalize()
            override fun delete() {
                runCatching { onDelete() }
            }
        }

        class LegacyTarget(
            val file: File,
            override val outputStream: OutputStream,
        ) : SaveTarget() {
            override fun finalize() = Unit
            override fun delete() {
                runCatching { if (file.exists()) file.delete() }
            }
        }
    }

    private companion object {
        const val LOG_TAG = "SaveRepository"
        const val SUBDIR = "Mods"
        const val EMIT_THRESHOLD_BYTES = 64L * 1024L
        const val MIME_TYPE = "application/octet-stream"
    }
}
