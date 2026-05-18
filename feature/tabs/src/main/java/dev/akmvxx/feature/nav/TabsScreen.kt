package dev.akmvxx.feature.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import dev.akmvxx.feature.nav.ui.BottomNavigationBar
import dev.akmvxx.navigation.tabsNavigator
import dev.akmvxx.ui.AppColors

@Composable
fun TabsScreen(
    builder: EntryProviderScope<NavKey>.() -> Unit,
) {
    val navigator = tabsNavigator()
    Box(
        modifier = Modifier.fillMaxSize()
            .background(AppColors.BackgroundPrimary)
    ){
        BottomNavigationBar()
        NavDisplay(
            backStack = navigator.backStack,
            onBack = { navigator.pop() },
            entryProvider = entryProvider(builder = builder),
        )
    }
}


