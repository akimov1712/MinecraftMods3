package dev.akmvxx.navigation

import androidx.navigation3.runtime.NavKey

sealed interface TabsNavKey: NavKey {

    data object Browse: TabsNavKey
    data object Favorite: TabsNavKey
    data object Propose: TabsNavKey
    data object Help: TabsNavKey

}