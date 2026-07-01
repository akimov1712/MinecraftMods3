package dev.akmvxx.feature.mod

import android.content.Context
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.akmvxx.android.MVI
import dev.akmvxx.android.SnackbarManager
import dev.akmvxx.common.errors.DataError
import dev.akmvxx.common.errors.Error
import dev.akmvxx.common.onError
import dev.akmvxx.common.onSuccess
import dev.akmvxx.domain.useCases.bug.ReportBugUseCase
import dev.akmvxx.domain.validation.bug.BugValidatorError
import dev.akmvxx.ui.R
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportDialogViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val reportBugUseCase: ReportBugUseCase,
    private val snackbarManager: SnackbarManager,
) : MVI<ReportDialogIntent, ReportDialogState, ReportDialogEvent>(ReportDialogState()) {

    private fun changeMessage(value: String) = _state.update {
        if (value.length <= MAX_MESSAGE_LENGTH) it.copy(message = value) else it
    }

    private fun submit() {
        val current = _state.value
        if (!current.canSubmit) return

        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            reportBugUseCase.report(
                email = current.email,
                message = current.message,
            ).onSuccess {
                snackbarManager.showMessage(context.getString(R.string.mod_report_success))
                _state.update { ReportDialogState() }
                _events.send(ReportDialogEvent.Submitted)
            }.onError { error, _ ->
                snackbarManager.showMessage(context.getString(errorMessageRes(error)))
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun errorMessageRes(error: Error): Int = when (error) {
        is BugValidatorError -> when (error) {
            BugValidatorError.EMAIL_EMPTY -> R.string.propose_error_email_required
            BugValidatorError.EMAIL_NOT_VALID -> R.string.propose_error_email_invalid
            BugValidatorError.MESSAGE_EMPTY -> R.string.propose_error_message_required
            BugValidatorError.MESSAGE_IS_SHORT -> R.string.mod_report_error_message_short
        }
        is DataError.Network -> when (error) {
            DataError.Network.REQUEST_TIMEOUT -> R.string.error_request_timeout
            DataError.Network.SERIALIZATION -> R.string.error_serialization
            DataError.Network.SERVER_ERROR -> R.string.error_server_error
            DataError.Network.NO_INTERNET -> R.string.error_no_internet
            DataError.Network.UNKNOWN -> R.string.error_unknown
            DataError.Network.INVALID_DATA -> R.string.error_invalid_data
            DataError.Network.BAD_REQUEST -> R.string.error_bad_request
            DataError.Network.NOT_FOUND -> R.string.error_not_found
        }
        else -> R.string.error_unknown
    }

    override suspend fun handleIntent(intent: ReportDialogIntent) {
        when (intent) {
            is ReportDialogIntent.ChangeMessage -> changeMessage(intent.value)
            ReportDialogIntent.Submit -> submit()
        }
    }

    private companion object {
        const val MAX_MESSAGE_LENGTH = 2000
    }
}
