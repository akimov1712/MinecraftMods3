package dev.akmvxx.feature.browse

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.akmvxx.navigation.TabsNavKey

fun EntryProviderScope<NavKey>.browseEntry() {
    entry<TabsNavKey.Browse> {
        BrowseScreen()
    }
}
