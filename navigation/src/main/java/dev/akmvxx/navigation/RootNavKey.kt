package dev.akmvxx.navigation

import androidx.navigation3.runtime.NavKey

sealed interface RootNavKey : NavKey {

    data object Splash : RootNavKey
    data object Tabs : RootNavKey
    data class ModDetail(val modId: Int) : RootNavKey
    data class Files(val modId: Int) : RootNavKey

}
