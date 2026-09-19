package ru.svolf.convertx.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.svolf.convertx.InMemoryDataStore
import ru.svolf.convertx.data.SettingsRepository
import ru.svolf.convertx.data.ThemeMode
import ru.svolf.convertx.runViewModelTest

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {
    @Test
    fun `settings actions update the exposed state`() = runViewModelTest {
        val viewModel = SettingsViewModel(SettingsRepository(InMemoryDataStore()))

        viewModel.setTheme(ThemeMode.DARK)
        viewModel.setFontSize(40)
        viewModel.setTwiceBack(false)
        advanceUntilIdle()

        val state = viewModel.state.first {
            it.theme == ThemeMode.DARK && it.fontSize == 32 && !it.twiceBackToExit
        }
        assertEquals(ThemeMode.DARK, state.theme)
        assertEquals(32, state.fontSize)
        assertEquals(false, state.twiceBackToExit)
        viewModel.setTwiceBack(true)
        advanceUntilIdle()
        viewModel.viewModelScope.cancel()
    }
}
