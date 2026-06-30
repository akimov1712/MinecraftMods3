package dev.akmvxx.feature.nav

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dev.akmvxx.navigation.TabsNavKey
import dev.akmvxx.ui.R

enum class Tabs(
    @StringRes val titleRes: Int,
    @DrawableRes val iconRes: Int,
    val navKey: TabsNavKey
) {

    Browse(
        titleRes = R.string.screen_browse,
        iconRes = R.drawable.ic_screen_browse,
        navKey = TabsNavKey.Browse
    ),

    Propose(
        titleRes = R.string.screen_propose,
        iconRes = R.drawable.ic_screen_propose,
        navKey = TabsNavKey.Propose
    ),

    Help(
        titleRes = R.string.screen_help,
        iconRes = R.drawable.ic_screen_help,
        navKey = TabsNavKey.Help
    ),

    Favorite(
        titleRes = R.string.screen_favorite,
        iconRes = R.drawable.ic_screen_favorite,
        navKey = TabsNavKey.Favorite
    ),

}
