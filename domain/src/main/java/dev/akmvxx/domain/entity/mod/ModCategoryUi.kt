package dev.akmvxx.domain.entity.mod

import androidx.annotation.StringRes
import dev.akmvxx.ui.R

enum class ModCategoryUi(@StringRes val titleRes: Int) {

    All(R.string.mod_category_all),
    Addon(R.string.mod_category_addon),
    Maps(R.string.mod_category_maps),
    Texture(R.string.mod_category_texture),
    Skin(R.string.mod_category_skins);


    fun toModCategory() = when(this){
        All -> null
        Addon -> ModCategory.Addon
        Maps -> ModCategory.Maps
        Texture -> ModCategory.Texture
        Skin -> ModCategory.Skin
    }
    companion object{
        fun fromModCategory(type: ModCategory) = when(type){
            ModCategory.Addon -> Addon
            ModCategory.Maps -> Maps
            ModCategory.Texture -> Texture
            ModCategory.Skin -> Skin
        }
    }

}