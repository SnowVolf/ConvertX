package ru.svolf.convertx.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.svolf.convertx.InMemoryHistoryDao
import ru.svolf.convertx.data.HistoryRecord
import ru.svolf.convertx.data.HistoryRepository
import ru.svolf.convertx.runViewModelTest

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class HistoryViewModelTest {
    @Test
    fun `records flow exposes repository updates`() = runViewModelTest {
        val dao = InMemoryHistoryDao()
        val repository = HistoryRepository(dao)
        val viewModel = HistoryViewModel(repository)
        val record = HistoryRecord(1L, 0, "input", "output", 0)

        val firstRecord = viewModel.records.first()
        assertEquals(emptyList<HistoryRecord>(), firstRecord)

        repository.save(record)
        assertEquals(listOf(record), viewModel.records.first { it == listOf(record) })
        viewModel.viewModelScope.cancel()
    }

    @Test
    fun `delete and clearAll delegate to repository`() = runViewModelTest {
        val repository = HistoryRepository(InMemoryHistoryDao())
        val viewModel = HistoryViewModel(repository)
        val first = HistoryRecord(1L, 0, "a", "b", 0)
        val second = HistoryRecord(2L, 1, "c", "d", 0)
        repository.save(first)
        repository.save(second)

        viewModel.delete(first)
        advanceUntilIdle()
        assertEquals(listOf(second), repository.records.first())

        viewModel.clearAll()
        advanceUntilIdle()
        assertEquals(emptyList<HistoryRecord>(), repository.records.first())
        viewModel.viewModelScope.cancel()
    }
}
