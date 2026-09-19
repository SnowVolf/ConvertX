package ru.svolf.convertx.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.svolf.convertx.InMemoryDataStore

class SettingsRepositoryTest {
    @Test
    fun `empty datastore exposes documented defaults`() = runTest {
        val state = SettingsRepository(InMemoryDataStore()).state.first()

        assertEquals(SettingsState(), state)
    }

    @Test
    fun `setters clamp values and persist all settings`() = runTest {
        val repository = SettingsRepository(InMemoryDataStore())

        repository.setTheme(ThemeMode.DARK)
        repository.setFontSize(99)
        repository.setTwiceBackToExit(false)
        repository.setBase64Mode(-1)
        repository.setHexMode(99)

        assertEquals(
            SettingsState(
                theme = ThemeMode.DARK,
                fontSize = 32,
                twiceBackToExit = false,
                base64Mode = 0,
                hexMode = 1
            ),
            repository.state.first()
        )
    }

    @Test
    fun `legacy font key is used when the new key is absent`() = runTest {
        val dataStore = InMemoryDataStore(
            preferencesOf(
                stringPreferencesKey("ITheme.Theme") to "1",
                intPreferencesKey("Font.Size") to 20,
                booleanPreferencesKey("Interaction.Back") to false,
                intPreferencesKey("spinner.base64") to 4,
                intPreferencesKey("spinner.hex") to 1
            )
        )

        assertEquals(
            SettingsState(ThemeMode.DARK, 20, false, 4, 1),
            SettingsRepository(dataStore).state.first()
        )
    }

    @Test
    fun `io failure is converted to empty preferences`() = runTest {
        val repository = SettingsRepository(
            object :
                androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> {
                override val data: Flow<androidx.datastore.preferences.core.Preferences> =
                    kotlinx.coroutines.flow.flow {
                        throw java.io.IOException("read failed")
                    }

                override suspend fun updateData(transform: suspend (androidx.datastore.preferences.core.Preferences) -> androidx.datastore.preferences.core.Preferences) =
                    error("not used")
            }
        )

        assertEquals(SettingsState(), repository.state.first())
    }
}
