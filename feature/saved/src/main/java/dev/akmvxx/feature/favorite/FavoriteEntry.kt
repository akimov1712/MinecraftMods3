package dev.akmvxx.feature.favorite

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.akmvxx.navigation.TabsNavKey

fun EntryProviderScope<NavKey>.favoriteEntry() {
    entry<TabsNavKey.Favorite> {
        FavoriteScreen()
    }
}
