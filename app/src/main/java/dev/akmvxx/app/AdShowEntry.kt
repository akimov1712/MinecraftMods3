package dev.akmvxx.app

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.akmvxx.ads.AdShowScreen
import dev.akmvxx.navigation.RootNavKey
import dev.akmvxx.navigation.rootNavigator

fun EntryProviderScope<NavKey>.adShowEntry() {
    entry<RootNavKey.AdShow> { key ->
        val navigator = rootNavigator()
        AdShowScreen(onClose = { navigator.replace(key.next) })
    }
}
