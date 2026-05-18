package dev.akmvxx.domain.entity.mod

enum class ModCategory {

    Texture,
    Addon,
    Skin,
    Maps;

    fun getExtensionFile(): String = when (this) {
        Skin -> ".mcpack"
        Maps -> ".mcworld"
        Texture -> ".mcpack"
        Addon -> ".mcaddon"
    }
}