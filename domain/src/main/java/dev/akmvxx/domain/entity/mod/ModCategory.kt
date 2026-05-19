package dev.akmvxx.domain.entity.mod

enum class ModCategory {

    Texture,
    Addon,
    Skin,
    Maps;

    override fun toString() = when(this){
        Texture -> "TEXTURE_PACK"
        Addon -> "ADDON"
        Skin -> "SKIN_PACK"
        Maps -> "WORLD"
    }

    fun getExtensionFile(): String = when (this) {
        Skin -> ".mcpack"
        Maps -> ".mcworld"
        Texture -> ".mcpack"
        Addon -> ".mcaddon"
    }
}