package dev.akmvxx.data.source.remote.settings

import com.google.gson.annotations.SerializedName
import dev.akmvxx.domain.entity.settings.AdChangePercentEntity
import dev.akmvxx.domain.entity.settings.AdEnabledEntity
import dev.akmvxx.domain.entity.settings.SettingsEntity
import dev.akmvxx.domain.entity.settings.SettingsEntity.NativeType

data class SettingsDto(
    @SerializedName("isOpenAdsEnabled") val openEnable: Boolean,
    @SerializedName("isNativeAdsEnabled") val nativeEnable: Boolean,
    @SerializedName("isInterAdsEnabled") val interEnable: Boolean,

    @SerializedName("delayInter") val cooldownAdSecond: Int,
    @SerializedName("countNativePreload") val preloadSize: Int,
    @SerializedName("adsInverval") val intervalNative: Int,
    @SerializedName("adsNativeType") val nativeType: String,

    @SerializedName("chanceShowOpenAds") val openChance: Int,
    @SerializedName("chanceShowNativeAds") val nativeChance: Int,
    @SerializedName("chanceShowInterAds") val interChance: Int,
){

    fun toEntity() = SettingsEntity(
        adEnabled = AdEnabledEntity(
            open = openEnable,
            native = nativeEnable,
            inter = interEnable
        ),
        adChangePercent = AdChangePercentEntity(
            open = openChance,
            native = nativeChance,
            inter = interChance
        ),
        cooldownAdSecond = cooldownAdSecond,
        preloadSize = preloadSize,
        intervalNative = intervalNative,
        nativeType = NativeType.toType(nativeType)
    )

}
