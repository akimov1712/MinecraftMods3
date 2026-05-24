package dev.akmvxx.feature.guide

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dev.akmvxx.ui.R

internal data class GuideStep(
    val number: Int,
    @StringRes val titleRes: Int,
    @StringRes val textRes: Int,
    // TODO: replace with real screenshots placed in core/ui/res/drawable/guide_step_N.png
    @DrawableRes val screenshotRes: Int = R.drawable.ic_image_placeholder,
)

internal val GuideSteps: List<GuideStep> = listOf(
    GuideStep(1, R.string.guide_step_1_title, R.string.guide_step_1_text),
    GuideStep(2, R.string.guide_step_2_title, R.string.guide_step_2_text),
    GuideStep(3, R.string.guide_step_3_title, R.string.guide_step_3_text),
    GuideStep(4, R.string.guide_step_4_title, R.string.guide_step_4_text),
    GuideStep(5, R.string.guide_step_5_title, R.string.guide_step_5_text),
    GuideStep(6, R.string.guide_step_6_title, R.string.guide_step_6_text),
    GuideStep(7, R.string.guide_step_7_title, R.string.guide_step_7_text),
    GuideStep(8, R.string.guide_step_8_title, R.string.guide_step_8_text),
    GuideStep(9, R.string.guide_step_9_title, R.string.guide_step_9_text),
)

data class GuideState(
    val placeholder: Boolean = true,
)

sealed interface GuideIntent

sealed interface GuideEvent
