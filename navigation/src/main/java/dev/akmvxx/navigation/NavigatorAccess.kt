package dev.akmvxx.navigation

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
interface NavigatorEntryPoint {
    fun rootNavigator(): RootNavigator
    fun tabsNavigator(): TabsNavigator
}

@Composable
fun rootNavigator(): RootNavigator {
    val activity = LocalContext.current.findComponentActivity()
    return remember(activity) {
        EntryPointAccessors
            .fromActivity(activity, NavigatorEntryPoint::class.java)
            .rootNavigator()
    }
}

@Composable
fun tabsNavigator(): TabsNavigator {
    val activity = LocalContext.current.findComponentActivity()
    return remember(activity) {
        EntryPointAccessors
            .fromActivity(activity, NavigatorEntryPoint::class.java)
            .tabsNavigator()
    }
}

private tailrec fun Context.findComponentActivity(): ComponentActivity = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findComponentActivity()
    else -> error("Navigator must be accessed within a ComponentActivity context")
}
