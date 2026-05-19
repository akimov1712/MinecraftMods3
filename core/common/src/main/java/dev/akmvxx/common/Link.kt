package dev.akmvxx.common

fun String.isLink(): Boolean {
    val value = trim()
    return value.startsWith(prefix = "http://", ignoreCase = true) ||
            value.startsWith(prefix = "https://", ignoreCase = true) ||
            value.startsWith(prefix = "www.", ignoreCase = true)
}
