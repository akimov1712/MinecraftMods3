package dev.akmvxx.feature.propose

data class ProposeState(
    val selectedTab: TabType = TabType.PROPOSE,
    val email: String = DEFAULT_EMAIL,
    val message: String = "",
    val isLoading: Boolean = false,
) {
    val canSubmit: Boolean
        get() = !isLoading && message.isNotBlank()

    enum class TabType { PROPOSE, FEEDBACK }

    companion object {
        const val DEFAULT_EMAIL = "test@mail.ru"
    }
}

sealed interface ProposeIntent {
    data class ChangeTab(val tab: ProposeState.TabType) : ProposeIntent
    data class ChangeMessage(val value: String) : ProposeIntent
    data object Submit : ProposeIntent
}

sealed interface ProposeEvent
