package dev.akmvxx.android

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SnackbarManager @Inject constructor() {

    private val _messages = Channel<String>()
    val messages = _messages.receiveAsFlow()

    fun showMessage(message: String) {
        _messages.trySend(message)
    }
}
