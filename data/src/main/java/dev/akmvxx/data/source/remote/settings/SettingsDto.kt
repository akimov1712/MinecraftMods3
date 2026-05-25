package dev.akmvxx.data.source.remote.settings

import com.google.gson.annotations.SerializedName

data class SettingsDto(
    @SerializedName("isOpenAdsEnabled") val openEnable: Boolean,
    @SerializedName("isInterAdsEnabled") val interEnable: Boolean,
    @SerializedName("isNativeAdsEnabled") val nativeEnable: Boolean,

    @SerializedName("delayInter") val delayInter: Int,
    @SerializedName("adsNativeType") val adsNativeType: String,
    @SerializedName("countNativePreload") val countNativePreload: Int,
    @SerializedName("adsInverval") val adsInverval: Int,

    @SerializedName("chanceShowOpenAds") val openChance: Int,
    @SerializedName("chanceShowInterAds") val interChance: Int,
    @SerializedName("chanceShowNativeAds") val nativeChance: Int,
)
