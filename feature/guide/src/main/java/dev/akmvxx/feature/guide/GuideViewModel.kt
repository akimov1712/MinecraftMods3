package dev.akmvxx.feature.guide

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.akmvxx.android.MVI
import javax.inject.Inject

@HiltViewModel
class GuideViewModel @Inject constructor() :
    MVI<GuideIntent, GuideState, GuideEvent>(GuideState()) {

    override suspend fun handleIntent(intent: GuideIntent) = Unit
}
