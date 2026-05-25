package dev.akmvxx.domain.useCases.settings

import dev.akmvxx.domain.repository.SettingsRepository

class GetSettingsUseCase(
    private val repository: SettingsRepository
) {

    suspend fun fetch() = repository.getSettings()

}