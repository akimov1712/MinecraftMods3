package dev.akmvxx.feature.mod

data class ReportDialogState(
    val email: String = DEFAULT_EMAIL,
    val message: String = "",
    val isLoading: Boolean = false,
) {
    val canSubmit: Boolean
        get() = !isLoading && message.isNotBlank()

    companion object {
        const val DEFAULT_EMAIL = "test@mail.ru"
    }
}

sealed interface ReportDialogIntent {
    data class ChangeMessage(val value: String) : ReportDialogIntent
    data object Submit : ReportDialogIntent
}

sealed interface ReportDialogEvent {
    data object Submitted : ReportDialogEvent
}
