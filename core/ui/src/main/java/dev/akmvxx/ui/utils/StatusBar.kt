package dev.akmvxx.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

enum class StatusBarColor{
    Light, Dark
}

@Composable
fun changeStatusBarColor(color: StatusBarColor) {
    val view = LocalView.current
    SideEffect {
        val window = (view.context as android.app.Activity).window
        WindowCompat.getInsetsController(window, view)
            .isAppearanceLightStatusBars = color == StatusBarColor.Dark
    }
}