package dev.akmvxx.common

import java.net.URLDecoder

/**
 * Extracts file name (with extension) from a URL or path string.
 *
 * Examples:
 *  - "https://edge.forgecdn.net/files/7614/696/Legends%20skin%20pack%20v1.7.mcpack"
 *      → "Legends skin pack v1.7.mcpack"
 *  - "https://api.mcpedl.com/api/download/781730/?r=eyJ..."
 *      → "781730"
 *
 * Returns null when the string has no usable last segment.
 */
fun String.extractFileName(): String? {
    if (isBlank()) return null
    val withoutQuery = substringBefore('?').substringBefore('#')
    val lastSegment = withoutQuery.trimEnd('/').substringAfterLast('/')
    if (lastSegment.isBlank()) return null
    return runCatching {
        URLDecoder.decode(lastSegment, Charsets.UTF_8.name())
    }.getOrDefault(lastSegment)
}

/**
 * Extracts file name without extension.
 *
 * Examples:
 *  - "Legends skin pack v1.7.mcpack" → "Legends skin pack v1.7"
 *  - "ActualGuns2.mcaddon"           → "ActualGuns2"
 */
fun String.extractFileNameWithoutExtension(): String? {
    val name = extractFileName() ?: return null
    return name.substringBeforeLast('.', missingDelimiterValue = name)
}

/**
 * Extracts file extension without the dot.
 *
 * Examples:
 *  - "Legends skin pack v1.7.mcpack" → "mcpack"
 *  - "ActualGuns2.mcaddon"           → "mcaddon"
 *  - "no_extension_file"             → null
 */
fun String.extractFileExtension(): String? {
    val name = extractFileName() ?: return null
    if (!name.contains('.')) return null
    return name.substringAfterLast('.')
}

/**
 * Extracts version segment from a URL or file name.
 *
 * Examples:
 *  - "Portal Gun Addon V4.1.mcaddon"      → "V4.1"
 *  - "Legends skin pack v1.7.mcpack"      → "v1.7"
 *  - "warhammer40kboltgun0.0.2.mcaddon"   → "0.0.2"
 *  - "ActualGuns2.mcaddon"                → null
 */
fun String.extractFileVersion(): String? {
    val source = extractFileName() ?: this
    return VERSION_REGEX.find(source)?.value
}

private val VERSION_REGEX = Regex("""[vV]?\d+\.\d+(?:\.\d+)?""")
