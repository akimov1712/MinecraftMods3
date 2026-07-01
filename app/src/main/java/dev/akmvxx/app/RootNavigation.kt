package dev.akmvxx.app

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import dev.akmvxx.feature.browse.browseEntry
import dev.akmvxx.feature.favorite.favoriteEntry
import dev.akmvxx.feature.files.filesEntry
import dev.akmvxx.feature.guide.guideEntry
import dev.akmvxx.feature.help.helpEntry
import dev.akmvxx.feature.mod.modEntry
import dev.akmvxx.feature.nav.TabsScreen
import dev.akmvxx.feature.propose.proposeEntry
import dev.akmvxx.feature.splash.SplashScreen
import dev.akmvxx.navigation.RootNavKey
import dev.akmvxx.navigation.rootNavigator

@Composable
fun RootNavigation() {
    val navigator = rootNavigator()
    NavDisplay(
        backStack = navigator.backStack,
        onBack = { navigator.pop() },
        entryProvider = entryProvider {
            entry<RootNavKey.Splash> {
                SplashScreen()
            }
            entry<RootNavKey.Tabs> {
                TabsScreen {
                    browseEntry()
                    favoriteEntry()
                    proposeEntry()
                    helpEntry()
                }
            }
            modEntry()
            filesEntry()
            guideEntry()
        },
    )
}
