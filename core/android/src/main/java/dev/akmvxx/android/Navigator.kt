package dev.akmvxx.android

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey

abstract class Navigator(initial: NavKey) {

    val backStack: SnapshotStateList<NavKey> = mutableStateListOf(initial)

    val current: NavKey?
        get() = backStack.lastOrNull()

    fun push(key: NavKey) {
        backStack.add(key)
    }

    fun pushOnTop(key: NavKey) {
        if (current != key) {
            backStack.add(key)
        }
    }

    fun replace(key: NavKey) {
        if (backStack.isEmpty()) {
            backStack.add(key)
        } else {
            backStack[backStack.lastIndex] = key
        }
    }

    fun replaceAll(key: NavKey) {
        backStack.clear()
        backStack.add(key)
    }

    fun pop(count: Int = 1) {
        repeat(count) {
            if (backStack.size > 1) {
                backStack.removeAt(backStack.lastIndex)
            }
        }
    }

    fun popToRoot() {
        while (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        }
    }
}
