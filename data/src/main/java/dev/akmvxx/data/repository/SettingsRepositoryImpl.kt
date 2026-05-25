package dev.akmvxx.data.repository

import android.content.Context
import dev.akmvxx.data.source.remote.settings.SettingsApi
import dev.akmvxx.domain.entity.settings.SettingsEntity
import dev.akmvxx.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val context: Context,
    private val api: SettingsApi
): SettingsRepository {

    override suspend fun getSettings(): SettingsEntity {
        TODO("Not yet implemented")
    }

}