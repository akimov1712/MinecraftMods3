package dev.akmvxx.feature.propose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.akmvxx.feature.propose.ProposeState.TabType
import dev.akmvxx.feature.propose.ui.LabeledField
import dev.akmvxx.feature.propose.ui.ProposeHeader
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R
import dev.akmvxx.ui.components.AppButton
import dev.akmvxx.ui.components.AppTabRow

@Composable
fun ProposeScreen(
    viewModel: ProposeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ProposeContent(
        state = state,
        onIntent = viewModel::sendIntent,
    )
}

@Composable
private fun ProposeContent(
    state: ProposeState,
    onIntent: (ProposeIntent) -> Unit,
) {
    val isFeedback = state.selectedTab == TabType.FEEDBACK

    val tabs = listOf(
        stringResource(R.string.propose_tab_propose),
        stringResource(R.string.propose_tab_feedback),
    )
    val messageLabel = stringResource(
        if (isFeedback) R.string.propose_field_feedback_label
        else R.string.propose_field_mod_label
    )
    val messageHint = stringResource(
        if (isFeedback) R.string.propose_field_feedback_hint
        else R.string.propose_field_mod_hint
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.BackgroundPrimary)
            .systemBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        ProposeHeader(
            title = stringResource(R.string.propose_title),
            subtitle = stringResource(R.string.propose_subtitle),
            modifier = Modifier.fillMaxWidth(),
        )

        AppTabRow(
            items = tabs,
            selectedIndex = state.selectedTab.ordinal,
            onTabSelected = { index ->
                onIntent(ProposeIntent.ChangeTab(TabType.entries[index]))
            },
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(0.dp),
        )

        LabeledField(
            label = stringResource(R.string.propose_field_email_label),
            value = state.email,
            onValueChange = { onIntent(ProposeIntent.ChangeEmail(it)) },
            placeholder = stringResource(R.string.propose_field_email_hint),
            enabled = !state.isLoading,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
        )

        LabeledField(
            label = messageLabel,
            value = state.message,
            onValueChange = { onIntent(ProposeIntent.ChangeMessage(it)) },
            placeholder = messageHint,
            singleLine = false,
            minLines = 4,
            maxLines = 8,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth(),
        )

        AppButton(
            text = stringResource(R.string.propose_send),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = state.canSubmit,
            isLoading = state.isLoading,
            startIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null,
                )
            },
            onClick = { onIntent(ProposeIntent.Submit) },
        )
    }
}
