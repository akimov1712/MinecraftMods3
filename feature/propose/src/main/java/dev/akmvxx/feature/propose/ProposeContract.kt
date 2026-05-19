package dev.akmvxx.feature.propose

data class ProposeState(
    val selectedTab: TabType = TabType.PROPOSE,
    val email: String = "",
    val message: String = "",
    val isLoading: Boolean = false,
) {
    val canSubmit: Boolean
        get() = !isLoading && email.isNotBlank() && message.isNotBlank()

    enum class TabType { PROPOSE, FEEDBACK }
}

sealed interface ProposeIntent {
    data class ChangeTab(val tab: ProposeState.TabType) : ProposeIntent
    data class ChangeEmail(val value: String) : ProposeIntent
    data class ChangeMessage(val value: String) : ProposeIntent
    data object Submit : ProposeIntent
}

sealed interface ProposeEvent
