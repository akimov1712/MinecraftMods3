package dev.akmvxx.feature.help

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.akmvxx.feature.help.ui.HelpHeader
import dev.akmvxx.feature.help.ui.HelpItem
import dev.akmvxx.feature.help.ui.SectionHeader
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R
import dev.akmvxx.ui.components.AppTextField

@Composable
fun HelpScreen(
    viewModel: HelpViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    HelpContent(
        state = state,
        onIntent = viewModel::sendIntent,
    )
}

@Composable
private fun HelpContent(
    state: HelpState,
    onIntent: (HelpIntent) -> Unit,
) {
    val context = LocalContext.current
    val filteredSections = remember(state.searchQuery) {
        if (state.searchQuery.isBlank()) {
            HelpSections
        } else {
            HelpSections.mapNotNull { section ->
                val matched = section.items.filter { item ->
                    val q = context.getString(item.questionRes)
                    val a = context.getString(item.answerRes)
                    q.contains(state.searchQuery, ignoreCase = true) ||
                            a.contains(state.searchQuery, ignoreCase = true)
                }
                if (matched.isEmpty()) null else section.copy(items = matched)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.BackgroundPrimary)
            .systemBarsPadding(),
    ) {
        HelpHeader(
            title = stringResource(R.string.help_title),
            subtitle = stringResource(R.string.help_subtitle),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 20.dp),
        )

        Spacer(Modifier.height(20.dp))

        AppTextField(
            value = state.searchQuery,
            onValueChange = { onIntent(HelpIntent.ChangeSearchQuery(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder = stringResource(R.string.help_search_placeholder),
            leadingIcon = Icons.Filled.Search,
        )

        Spacer(Modifier.height(16.dp))

        if (filteredSections.isEmpty()) {
            EmptyResults(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 0.dp,
                    bottom = 100.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                filteredSections.forEachIndexed { sectionIndex, section ->
                    item(key = "section_${section.id}") {
                        SectionHeader(
                            title = stringResource(section.titleRes),
                            modifier = Modifier.padding(
                                top = if (sectionIndex == 0) 0.dp else 14.dp,
                                bottom = 4.dp,
                                start = 4.dp,
                            ),
                        )
                    }
                    items(items = section.items, key = { "${section.id}_${it.id}" }) { item ->
                        HelpItem(
                            question = stringResource(item.questionRes),
                            answer = stringResource(item.answerRes),
                            expanded = state.expandedItemId == item.id,
                            onToggle = { onIntent(HelpIntent.ToggleExpand(item.id)) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyResults(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.help_no_results),
            color = AppColors.TextWhite.copy(alpha = 0.5f),
            fontSize = 14.sp,
        )
    }
}
