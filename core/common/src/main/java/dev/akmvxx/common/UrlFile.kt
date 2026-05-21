package dev.akmvxx.common

import java.net.URLDecoder

fun String.extractFileName(): String? {
    if (isBlank()) return null
    val withoutQuery = substringBefore('?').substringBefore('#')
    val lastSegment = withoutQuery.trimEnd('/').substringAfterLast('/')
    if (lastSegment.isBlank()) return null
    return runCatching {
        URLDecoder.decode(lastSegment, Charsets.UTF_8.name())
    }.getOrDefault(lastSegment)
}

fun String.extractFileNameWithoutExtension(): String? {
    val name = extractFileName() ?: return null
    return name.substringBeforeLast('.', missingDelimiterValue = name)
}

fun String.extractFileExtension(): String? {
    val name = extractFileName() ?: return null
    if (!name.contains('.')) return null
    return name.substringAfterLast('.')
}

fun String.extractFileNameOrFallback(extensionFallback: String): String? {
    val name = extractFileName() ?: return null
    if (name.contains('.')) return name
    return "$name.${extensionFallback.removePrefix(".")}"
}

fun String.extractFileVersion(): String? {
    val source = extractFileName() ?: this
    return VERSION_REGEX.find(source)?.value
}

private val VERSION_REGEX = Regex("""[vV]?\d+\.\d+(?:\.\d+)?""")
