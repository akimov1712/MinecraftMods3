package dev.akmvxx.domain.entity.mod

enum class ModCategory {

    Texture,
    Addon,
    Skin,
    Maps;

    override fun toString() = when(this){
        Texture -> "TEXTURE"
        Addon -> "ADDON"
        Skin -> "SKIN"
        Maps -> "MAPS"
    }

    fun getExtensionFile(): String = when (this) {
        Skin -> ".mcpack"
        Maps -> ".mcworld"
        Texture -> ".mcpack"
        Addon -> ".mcaddon"
    }
}