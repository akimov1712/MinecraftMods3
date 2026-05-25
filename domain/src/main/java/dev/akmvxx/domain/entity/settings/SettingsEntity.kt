package dev.akmvxx.domain.entity.settings

import android.os.Parcelable

data class SettingsEntity(
   val adEnabled: AdEnabledEntity,
   val adChangePercent: AdChangePercentEntity,

   val cooldownAdSecond: Int,
   val preloadSize: Int,
   val intervalNative: Int,
   val nativeType: NativeTypeEnum
){

    enum class NativeTypeEnum{

        BANNER,
        NATIVE;

        companion object{
            fun toType(value: String?) = value?.let { valueOf(it) } ?: NATIVE
        }

    }

}