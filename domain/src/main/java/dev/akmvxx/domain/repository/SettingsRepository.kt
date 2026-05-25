package dev.akmvxx.domain.repository

import dev.akmvxx.common.Result
import dev.akmvxx.common.errors.DataError
import dev.akmvxx.domain.entity.settings.SettingsEntity

interface SettingsRepository {

    suspend fun getSettings(): Result<SettingsEntity, DataError>

}