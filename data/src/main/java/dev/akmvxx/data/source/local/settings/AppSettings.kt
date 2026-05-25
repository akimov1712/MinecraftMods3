package dev.akmvxx.data.source.local.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Storage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AppSettings(private val context: Context) {

    suspend fun put(key: SettingsKey, value: String) {
        context.dataStore.edit {
            it[stringPreferencesKey(key.toString())] = value
        }
    }

    suspend fun read(key: SettingsKey, defaultValue: String?): String? {
        return context.dataStore.data.map {
            it[stringPreferencesKey(key.toString())] ?: defaultValue
        }.first()
    }

}