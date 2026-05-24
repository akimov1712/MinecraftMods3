package dev.akmvxx.feature.guide

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.akmvxx.navigation.RootNavKey

fun EntryProviderScope<NavKey>.guideEntry() {
    entry<RootNavKey.InstallGuide> {
        GuideScreen()
    }
}
