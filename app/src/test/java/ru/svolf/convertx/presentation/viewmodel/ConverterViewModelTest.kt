package ru.svolf.convertx.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ru.svolf.convertx.InMemoryDataStore
import ru.svolf.convertx.InMemoryHistoryDao
import ru.svolf.convertx.data.ConverterEngine
import ru.svolf.convertx.data.HistoryRepository
import ru.svolf.convertx.data.SettingsRepository
import ru.svolf.convertx.presentation.navigation.Base64Route
import ru.svolf.convertx.presentation.navigation.UnicodeRoute
import ru.svolf.convertx.runViewModelTest

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class ConverterViewModelTest {
    @Test
    fun `route arguments initialize input output and mode`() = runViewModelTest {
        val settings = SettingsRepository(InMemoryDataStore())
        val viewModel = ConverterViewModel(
            Base64Route(input = "in", output = "out", mode = 3),
            HistoryRepository(InMemoryHistoryDao()),
            settings,
            ConverterEngine()
        )

        assertEquals("in", viewModel.state.value.input)
        assertEquals("out", viewModel.state.value.output)
        assertEquals(3, viewModel.state.value.mode)
        viewModel.viewModelScope.cancel()
    }

    @Test
    fun `input conversion updates output and saves history`() = runViewModelTest {
        val history = HistoryRepository(InMemoryHistoryDao())
        val viewModel = ConverterViewModel(
            UnicodeRoute,
            history,
            SettingsRepository(InMemoryDataStore()),
            ConverterEngine()
        )

        viewModel.onInputChanged("A")
        finishConversion()

        assertEquals("A", viewModel.state.value.input)
        assertEquals("\\u0041", viewModel.state.value.output)
        assertFalse(viewModel.state.value.busy)
        assertEquals(1L, viewModel.state.value.conversionPulse)
        assertEquals(0, history.records.first().first().decoder)
        viewModel.viewModelScope.cancel()
    }

    @Test
    fun `output conversion highlights input`() = runViewModelTest {
        val viewModel = ConverterViewModel(
            UnicodeRoute,
            HistoryRepository(InMemoryHistoryDao()),
            SettingsRepository(InMemoryDataStore()),
            ConverterEngine()
        )

        viewModel.onOutputChanged("\\u0041")
        finishConversion()

        assertEquals("A", viewModel.state.value.input)
        assertEquals("\\u0041", viewModel.state.value.output)
        assertTrue(viewModel.state.value.highlightInput)
        viewModel.viewModelScope.cancel()
    }

    @Test
    fun `invalid conversion exposes an error and does not throw`() = runViewModelTest {
        val viewModel = ConverterViewModel(
            UnicodeRoute,
            HistoryRepository(InMemoryHistoryDao()),
            SettingsRepository(InMemoryDataStore()),
            ConverterEngine()
        )

        viewModel.onOutputChanged("\\u12x4")
        finishConversion()

        assertNotNull(viewModel.state.value.error)
        assertFalse(viewModel.state.value.busy)
        viewModel.viewModelScope.cancel()
    }

    @Test
    fun `clear cancels a pending conversion`() = runViewModelTest {
        val history = HistoryRepository(InMemoryHistoryDao())
        val viewModel = ConverterViewModel(
            UnicodeRoute,
            history,
            SettingsRepository(InMemoryDataStore()),
            ConverterEngine()
        )

        viewModel.onInputChanged("A")
        viewModel.clear()
        advanceUntilIdle()

        assertEquals("", viewModel.state.value.input)
        assertEquals("", viewModel.state.value.output)
        assertTrue(history.records.first().isEmpty())
        viewModel.viewModelScope.cancel()
    }

    @Test
    fun `mode change is persisted for base64 routes`() = runViewModelTest {
        val dataStore = InMemoryDataStore()
        val viewModel = ConverterViewModel(
            Base64Route(),
            HistoryRepository(InMemoryHistoryDao()),
            SettingsRepository(dataStore),
            ConverterEngine()
        )

        viewModel.onModeChanged(4)
        advanceUntilIdle()

        assertEquals(4, viewModel.state.value.mode)
        assertEquals(4, SettingsRepository(dataStore).state.first().base64Mode)
        viewModel.viewModelScope.cancel()
    }

    private suspend fun kotlinx.coroutines.test.TestScope.finishConversion() {
        runCurrent()
        advanceUntilIdle()
        Thread.sleep(100)
        runCurrent()
        advanceUntilIdle()
    }
}
