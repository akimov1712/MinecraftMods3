package dev.akmvxx.feature.guide

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dev.akmvxx.ui.R

internal data class GuideStep(
    val number: Int,
    @StringRes val titleRes: Int,
    @StringRes val textRes: Int,
    @DrawableRes val screenshotRes: Int?,
)

internal val GuideSteps: List<GuideStep> = listOf(
    GuideStep(
        1,
        R.string.guide_step_1_title,
        R.string.guide_step_1_text,
        R.drawable.guide_step_1
    ),
    GuideStep(
        2,
        R.string.guide_step_2_title,
        R.string.guide_step_2_text,
        R.drawable.guide_step_2
    ),
    GuideStep(
        3,
        R.string.guide_step_3_title,
        R.string.guide_step_3_text,
        R.drawable.guide_step_3
    ),
    GuideStep(
        4,
        R.string.guide_step_4_title,
        R.string.guide_step_4_text,
        R.drawable.guide_step_4
    ),
    GuideStep(
        5,
        R.string.guide_step_5_title,
        R.string.guide_step_5_text,
        R.drawable.guide_step_5
    ),
    GuideStep(
        6,
        R.string.guide_step_6_title,
        R.string.guide_step_6_text,
        R.drawable.guide_step_6
    ),
    GuideStep(
        7,
        R.string.guide_step_7_title,
        R.string.guide_step_7_text,
        R.drawable.guide_step_7
    ),
    GuideStep(
        8,
        R.string.guide_step_8_title,
        R.string.guide_step_8_text,
        null
    ),
)

data class GuideState(
    val placeholder: Boolean = true,
)

sealed interface GuideIntent

sealed interface GuideEvent
