package dev.akmvxx.domain.entity.settings

data class SettingsEntity(
   val adEnabled: AdEnabledEntity,
   val adChangePercent: AdChangePercentEntity,

   val cooldownAdSecond: Int,
   val preloadSize: Int,
   val intervalNative: Int,
   val nativeType: NativeType
){

    enum class NativeType{

        BANNER,
        NATIVE;

        companion object{
            fun toType(value: String?) = value?.let { valueOf(it) } ?: NATIVE
        }

    }

}
