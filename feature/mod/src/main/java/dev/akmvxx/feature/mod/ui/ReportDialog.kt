package dev.akmvxx.feature.mod.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.akmvxx.feature.mod.ReportDialogEvent
import dev.akmvxx.feature.mod.ReportDialogIntent
import dev.akmvxx.feature.mod.ReportDialogState
import dev.akmvxx.feature.mod.ReportDialogViewModel
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R
import dev.akmvxx.ui.components.AppButton
import dev.akmvxx.ui.components.AppTextField

@Composable
internal fun ReportDialog(
    modTitle: String,
    onDismiss: () -> Unit,
    viewModel: ReportDialogViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ReportDialogEvent.Submitted -> onDismiss()
            }
        }
    }

    Dialog(
        onDismissRequest = { if (!state.isLoading) onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = !state.isLoading,
            dismissOnClickOutside = !state.isLoading,
            usePlatformDefaultWidth = false,
        ),
    ) {
        ReportDialogContent(
            modTitle = modTitle,
            state = state,
            onIntent = viewModel::sendIntent,
            onDismiss = onDismiss,
        )
    }
}

@Composable
private fun ReportDialogContent(
    modTitle: String,
    state: ReportDialogState,
    onIntent: (ReportDialogIntent) -> Unit,
    onDismiss: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(AppColors.BackgroundSecondary)
            .border(
                width = 1.dp,
                color = AppColors.Outlined,
                shape = RoundedCornerShape(24.dp),
            )
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = stringResource(R.string.mod_report_title),
                color = AppColors.TextWhite,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
            )
            Text(
                text = subtitle(modTitle),
                color = AppColors.TextWhite.copy(alpha = 0.6f),
                fontSize = 14.sp,
                lineHeight = 19.sp,
            )
        }

        Field(
            label = stringResource(R.string.propose_field_email_label),
            value = state.email,
            onValueChange = { onIntent(ReportDialogIntent.ChangeEmail(it)) },
            placeholder = stringResource(R.string.propose_field_email_hint),
            enabled = !state.isLoading,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )

        Field(
            label = stringResource(R.string.mod_report_message_label),
            value = state.message,
            onValueChange = { onIntent(ReportDialogIntent.ChangeMessage(it)) },
            placeholder = stringResource(R.string.mod_report_message_hint),
            enabled = !state.isLoading,
            singleLine = false,
            minLines = 4,
            maxLines = 6,
        )

        Spacer(Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            AppButton(
                text = stringResource(R.string.mod_report_cancel),
                onClick = onDismiss,
                enabled = !state.isLoading,
                containerColor = AppColors.BackgroundPrimary,
                contentColor = AppColors.TextWhite,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
            )
            AppButton(
                text = stringResource(R.string.mod_report_send),
                onClick = { onIntent(ReportDialogIntent.Submit) },
                enabled = state.canSubmit,
                isLoading = state.isLoading,
                modifier = Modifier
                    .weight(1.4f)
                    .height(48.dp),
            )
        }
    }
}

@Composable
private fun subtitle(modTitle: String): String =
    if (modTitle.isBlank()) {
        stringResource(R.string.mod_report_subtitle_generic)
    } else {
        stringResource(R.string.mod_report_subtitle, modTitle)
    }

@Composable
private fun Field(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    enabled: Boolean,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            color = AppColors.TextWhite,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
        )
        AppTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = placeholder,
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            enabled = enabled,
            keyboardOptions = keyboardOptions,
        )
    }
}
