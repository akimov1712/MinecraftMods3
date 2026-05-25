package dev.akmvxx.data.repository

import android.content.Context
import dev.akmvxx.common.Result
import dev.akmvxx.common.errors.DataError
import dev.akmvxx.data.BuildConfig
import dev.akmvxx.data.exceptionWrapper
import dev.akmvxx.data.source.local.settings.AppSettings
import dev.akmvxx.data.source.local.settings.SettingsKey
import dev.akmvxx.data.source.remote.settings.SettingsApi
import dev.akmvxx.data.source.remote.settings.SettingsDto
import dev.akmvxx.domain.entity.settings.AdChangePercentEntity
import dev.akmvxx.domain.entity.settings.AdEnabledEntity
import dev.akmvxx.domain.entity.settings.SettingsEntity
import dev.akmvxx.domain.entity.settings.SettingsEntity.NativeType
import dev.akmvxx.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val context: Context,
    private val api: SettingsApi,
    private val appSettings: AppSettings
): SettingsRepository {

    private suspend fun loadSettings(): SettingsEntity? {
        val response = api.loadConfiguration(BuildConfig.APP_ID)
        val result = response.body()
        return result?.sdk?.toEntity().takeIf { response.isSuccessful }
    }

    override suspend fun getSettings(): Result<SettingsEntity, DataError> =
        context.exceptionWrapper(data = readSettings()) {
            val settings = loadSettings()
            settings?.let { saveSettings(it) }
            Result.Success(readSettings())
        }

    private suspend fun saveSettings(setting: SettingsEntity){
        appSettings.put(SettingsKey.ENABLE_OPEN, setting.adEnabled.open.toString())
        appSettings.put(SettingsKey.ENABLE_INTER, setting.adEnabled.inter.toString())
        appSettings.put(SettingsKey.ENABLE_NATIVE, setting.adEnabled.native.toString())
        appSettings.put(SettingsKey.COOLDOWN_AD_SECOND, setting.cooldownAdSecond.toString())
        appSettings.put(SettingsKey.PRELOAD_SIZE, setting.preloadSize.toString())
        appSettings.put(SettingsKey.INTERVAL_NATIVE, setting.intervalNative.toString())
        appSettings.put(SettingsKey.NATIVE_TYPE, setting.nativeType.name)
        appSettings.put(SettingsKey.CHANCE_OPEN, setting.adChangePercent.open.toString())
        appSettings.put(SettingsKey.CHANCE_INTERSTITIAL, setting.adChangePercent.inter.toString())
        appSettings.put(SettingsKey.CHANCE_NATIVE, setting.adChangePercent.native.toString())
    }

    private suspend fun readSettings() = SettingsEntity(
        adEnabled = AdEnabledEntity(
            open = appSettings.read(SettingsKey.ENABLE_OPEN, null)?.toBoolean() ?: false,
            native = appSettings.read(SettingsKey.ENABLE_NATIVE, null)?.toBoolean() ?: false,
            inter = appSettings.read(SettingsKey.ENABLE_INTER, null)?.toBoolean() ?: false
        ),
        adChangePercent = AdChangePercentEntity(
            open = appSettings.read(SettingsKey.CHANCE_OPEN, null)?.toInt() ?: 100,
            native = appSettings.read(SettingsKey.CHANCE_NATIVE, null)?.toInt() ?: 100,
            inter = appSettings.read(SettingsKey.CHANCE_INTERSTITIAL, null)?.toInt() ?: 100
        ),
        cooldownAdSecond = appSettings.read(SettingsKey.COOLDOWN_AD_SECOND, null)?.toInt() ?: 60,
        preloadSize = appSettings.read(SettingsKey.PRELOAD_SIZE, null)?.toInt() ?: 3,
        intervalNative = appSettings.read(SettingsKey.INTERVAL_NATIVE, null)?.toInt() ?: 3,
        nativeType = NativeType.toType(appSettings.read(SettingsKey.NATIVE_TYPE, null))
    )

}