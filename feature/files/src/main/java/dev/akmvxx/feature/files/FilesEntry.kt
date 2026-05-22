package dev.akmvxx.feature.files

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.akmvxx.navigation.RootNavKey

fun EntryProviderScope<NavKey>.filesEntry() {
    entry<RootNavKey.Files> { key ->
        FilesScreen(modId = key.modId)
    }
}
