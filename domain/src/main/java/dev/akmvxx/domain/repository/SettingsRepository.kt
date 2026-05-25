package dev.akmvxx.domain.repository

import dev.akmvxx.domain.entity.settings.SettingsEntity

interface SettingsRepository {

    suspend fun getSettings(): SettingsEntity

}