package dev.akmvxx.android

import java.util.Locale


fun deviceLanguage(): String {
    val language = Locale.getDefault().language
    val languages = setOf("ru", "de", "es", "fr", "hi", "it", "ja", "ko", "pt", "ar", "en")
    return if (languages.contains(language)) language else "en"
}