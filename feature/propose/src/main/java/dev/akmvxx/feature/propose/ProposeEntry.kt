package dev.akmvxx.feature.propose

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.akmvxx.navigation.TabsNavKey

fun EntryProviderScope<NavKey>.proposeEntry() {
    entry<TabsNavKey.Propose> {
        ProposeScreen()
    }
}
