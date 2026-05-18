package dev.akmvxx.feature.help

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.akmvxx.navigation.TabsNavKey

fun EntryProviderScope<NavKey>.helpEntry() {
    entry<TabsNavKey.Help> {
        HelpScreen()
    }
}
