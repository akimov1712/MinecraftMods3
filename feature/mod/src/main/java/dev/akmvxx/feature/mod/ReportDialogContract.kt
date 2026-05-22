package dev.akmvxx.feature.mod

data class ReportDialogState(
    val email: String = "",
    val message: String = "",
    val isLoading: Boolean = false,
) {
    val canSubmit: Boolean
        get() = !isLoading && email.isNotBlank() && message.isNotBlank()
}

sealed interface ReportDialogIntent {
    data class ChangeEmail(val value: String) : ReportDialogIntent
    data class ChangeMessage(val value: String) : ReportDialogIntent
    data object Submit : ReportDialogIntent
}

sealed interface ReportDialogEvent {
    data object Submitted : ReportDialogEvent
}
