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

private const val DefaultFontSize = 16
private const val MinFontSize = 12
private const val MaxFontSize = 32
private const val DefaultBase64Mode = 0
private const val MaxBase64Mode = 5
private const val DefaultHexMode = 0
private const val MaxHexMode = 1

enum class ThemeMode { LIGHT, DARK }

data class SettingsState(
    val theme: ThemeMode = ThemeMode.LIGHT,
    val fontSize: Int = DefaultFontSize,
    val twiceBackToExit: Boolean = true,
    val base64Mode: Int = DefaultBase64Mode,
    val hexMode: Int = DefaultHexMode
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
                theme = if (preferences[Keys.theme]?.toIntOrNull() == ThemeMode.DARK.ordinal) {
                    ThemeMode.DARK
                } else {
                    ThemeMode.LIGHT
                },
                fontSize = (preferences[Keys.appFontSize] ?: preferences[Keys.oldFontSize]
                ?: DefaultFontSize).coerceIn(MinFontSize, MaxFontSize),
                twiceBackToExit = preferences[Keys.twiceBack] ?: true,
                base64Mode = (preferences[Keys.base64Mode] ?: DefaultBase64Mode)
                    .coerceIn(DefaultBase64Mode, MaxBase64Mode),
                hexMode = (preferences[Keys.hexMode] ?: DefaultHexMode)
                    .coerceIn(DefaultHexMode, MaxHexMode)
            )
        }

    suspend fun setTheme(theme: ThemeMode) =
        dataStore.edit { it[Keys.theme] = theme.ordinal.toString() }

    suspend fun setFontSize(size: Int) =
        dataStore.edit { it[Keys.appFontSize] = size.coerceIn(MinFontSize, MaxFontSize) }

    suspend fun setTwiceBackToExit(enabled: Boolean) =
        dataStore.edit { it[Keys.twiceBack] = enabled }

    suspend fun setBase64Mode(mode: Int) =
        dataStore.edit { it[Keys.base64Mode] = mode.coerceIn(DefaultBase64Mode, MaxBase64Mode) }

    suspend fun setHexMode(mode: Int) =
        dataStore.edit { it[Keys.hexMode] = mode.coerceIn(DefaultHexMode, MaxHexMode) }
}
