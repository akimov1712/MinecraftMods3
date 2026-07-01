package dev.akmvxx.feature.propose

import android.R.attr.text
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
import dev.akmvxx.domain.useCases.propose.ProposeModUseCase
import dev.akmvxx.domain.validation.bug.BugValidatorError
import dev.akmvxx.domain.validation.propose.ProposeValidatorError
import dev.akmvxx.feature.propose.ProposeState.TabType
import dev.akmvxx.ui.R
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.copy

@HiltViewModel
class ProposeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val proposeModUseCase: ProposeModUseCase,
    private val reportBugUseCase: ReportBugUseCase,
    private val snackbarManager: SnackbarManager,
) : MVI<ProposeIntent, ProposeState, ProposeEvent>(ProposeState()) {

    private fun changeTab(tab: TabType) = _state.update { it.copy(selectedTab = tab) }
    private fun changeMessage(value: String) = _state.update { if (value.length <= 2000) it.copy(message = value) else it }

    private fun submit() {
        val current = _state.value
        if (!current.canSubmit) return

        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = when (current.selectedTab) {
                TabType.PROPOSE -> proposeModUseCase.report(
                    email = current.email,
                    message = current.message,
                )
                TabType.FEEDBACK -> reportBugUseCase.report(
                    email = current.email,
                    message = current.message,
                )
            }
            result.onSuccess {
                snackbarManager.showMessage(context.getString(R.string.propose_success))
                _state.update {
                    it.copy(message = "", isLoading = false)
                }
            }.onError { error, _ ->
                snackbarManager.showMessage(context.getString(errorMessageRes(error)))
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun errorMessageRes(error: Error): Int = when (error) {
        is ProposeValidatorError -> proposeValidationMessage(error)
        is BugValidatorError -> bugValidationMessage(error)
        is DataError.Network -> networkMessage(error)
        else -> R.string.error_unknown
    }

    private fun proposeValidationMessage(error: ProposeValidatorError): Int = when (error) {
        ProposeValidatorError.EMAIL_EMPTY -> R.string.propose_error_email_required
        ProposeValidatorError.EMAIL_NOT_VALID -> R.string.propose_error_email_invalid
        ProposeValidatorError.MESSAGE_EMPTY -> R.string.propose_error_message_required
        ProposeValidatorError.MESSAGE_IS_SHORT -> R.string.propose_error_message_short
    }

    private fun bugValidationMessage(error: BugValidatorError): Int = when (error) {
        BugValidatorError.EMAIL_EMPTY -> R.string.propose_error_email_required
        BugValidatorError.EMAIL_NOT_VALID -> R.string.propose_error_email_invalid
        BugValidatorError.MESSAGE_EMPTY -> R.string.propose_error_message_required
        BugValidatorError.MESSAGE_IS_SHORT -> R.string.propose_error_message_short
    }

    private fun networkMessage(error: DataError.Network): Int = when (error) {
        DataError.Network.REQUEST_TIMEOUT -> R.string.error_request_timeout
        DataError.Network.SERIALIZATION -> R.string.error_serialization
        DataError.Network.SERVER_ERROR -> R.string.error_server_error
        DataError.Network.NO_INTERNET -> R.string.error_no_internet
        DataError.Network.UNKNOWN -> R.string.error_unknown
        DataError.Network.INVALID_DATA -> R.string.error_invalid_data
        DataError.Network.BAD_REQUEST -> R.string.error_bad_request
        DataError.Network.NOT_FOUND -> R.string.error_not_found
    }

    override suspend fun handleIntent(intent: ProposeIntent) {
        when (intent) {
            is ProposeIntent.ChangeTab -> changeTab(intent.tab)
            is ProposeIntent.ChangeMessage -> changeMessage(intent.value)
            ProposeIntent.Submit -> submit()
        }
    }
}
