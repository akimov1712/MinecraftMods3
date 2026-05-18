package dev.akmvxx.android

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

//val snackbarModule = module {
//    single { SnackbarManager() }
//}

class SnackbarManager {

    private val _messages = Channel<String>()
    val messages = _messages.receiveAsFlow()

    fun showMessage(message: String){
        _messages.trySend(message)
    }

}