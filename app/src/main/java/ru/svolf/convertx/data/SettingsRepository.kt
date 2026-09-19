package ru.svolf.convertx.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

enum class ThemeMode { LIGHT, DARK }

data class SettingsState(
    val theme: ThemeMode = ThemeMode.LIGHT,
    val fontSize: Int = 16,
    val twiceBackToExit: Boolean = true,
    val base64Mode: Int = 0,
    val hexMode: Int = 0
)

class SettingsRepository(private val dataStore: DataStore<Preferences>) {
    private object Keys {
        val theme = stringPreferencesKey("ITheme.Theme")
        val appFontSize = intPreferencesKey("AppFontSize")
        val oldFontSize = intPreferencesKey("Font.Size")
        val twiceBack = booleanPreferencesKey("Interaction.Back")
        val base64Mode = intPreferencesKey("spinner.base64")
        val hexMode = intPreferencesKey("spinner.hex")
    }

    val state: Flow<SettingsState> = dataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { preferences ->
            SettingsState(
                theme = if (preferences[Keys.theme]?.toIntOrNull() == ThemeMode.DARK.ordinal) ThemeMode.DARK else ThemeMode.LIGHT,
                fontSize = (preferences[Keys.appFontSize] ?: preferences[Keys.oldFontSize]
                ?: 16).coerceIn(12, 32),
                twiceBackToExit = preferences[Keys.twiceBack] ?: true,
                base64Mode = (preferences[Keys.base64Mode] ?: 0).coerceIn(0, 5),
                hexMode = (preferences[Keys.hexMode] ?: 0).coerceIn(0, 1)
            )
        }

    suspend fun setTheme(theme: ThemeMode) =
        dataStore.edit { it[Keys.theme] = theme.ordinal.toString() }

    suspend fun setFontSize(size: Int) =
        dataStore.edit { it[Keys.appFontSize] = size.coerceIn(12, 32) }

    suspend fun setTwiceBackToExit(enabled: Boolean) =
        dataStore.edit { it[Keys.twiceBack] = enabled }

    suspend fun setBase64Mode(mode: Int) =
        dataStore.edit { it[Keys.base64Mode] = mode.coerceIn(0, 5) }

    suspend fun setHexMode(mode: Int) = dataStore.edit { it[Keys.hexMode] = mode.coerceIn(0, 1) }
}
