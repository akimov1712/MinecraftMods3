package dev.akmvxx.feature.mod

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import dev.akmvxx.navigation.RootNavKey

fun EntryProviderScope<NavKey>.modEntry() {
    entry<RootNavKey.ModDetail> { key ->
        ModScreen(modId = key.modId)
    }
}
