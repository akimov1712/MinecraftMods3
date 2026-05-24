package dev.akmvxx.feature.guide

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.akmvxx.feature.guide.ui.GuideHeader
import dev.akmvxx.feature.guide.ui.StepCard
import dev.akmvxx.navigation.rootNavigator
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R

@Composable
fun GuideScreen(
    viewModel: GuideViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navigator = rootNavigator()
    GuideContent(
        state = state,
        onBack = { navigator.pop() },
        onIntent = viewModel::sendIntent,
    )
}

@Composable
private fun GuideContent(
    state: GuideState,
    onBack: () -> Unit,
    onIntent: (GuideIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.BackgroundPrimary)
            .systemBarsPadding(),
    ) {
        GuideHeader(
            title = stringResource(R.string.guide_title),
            subtitle = stringResource(R.string.guide_subtitle),
            onBack = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 12.dp),
        )

        Spacer(Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 0.dp,
                bottom = 40.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            items(items = GuideSteps, key = { it.number }) { step ->
                StepCard(
                    number = step.number,
                    title = stringResource(step.titleRes),
                    text = stringResource(step.textRes),
                    screenshotRes = step.screenshotRes,
                    isPlaceholder = step.screenshotRes == R.drawable.ic_image_placeholder,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            item(key = "footer") {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.guide_footer),
                    color = AppColors.TextWhite.copy(alpha = 0.45f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
